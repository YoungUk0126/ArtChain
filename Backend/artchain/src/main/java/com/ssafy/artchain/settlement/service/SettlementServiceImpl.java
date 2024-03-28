package com.ssafy.artchain.settlement.service;

import com.ssafy.artchain.funding.entity.Funding;
import com.ssafy.artchain.funding.entity.FundingProgressStatus;
import com.ssafy.artchain.funding.repository.FundingRepository;
import com.ssafy.artchain.member.dto.CustomUserDetails;
import com.ssafy.artchain.s3.S3Service;
import com.ssafy.artchain.settlement.dto.SettlementListItemDto;
import com.ssafy.artchain.settlement.dto.SettlementRequestDto;
import com.ssafy.artchain.settlement.dto.SettlementResponseDto;
import com.ssafy.artchain.settlement.entity.Settlement;
import com.ssafy.artchain.settlement.entity.SettlementStatus;
import com.ssafy.artchain.settlement.repository.SettlementRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class SettlementServiceImpl implements SettlemnetService {

    private final SettlementRepository settlementRepository;
    private final FundingRepository fundingRepository;
    private final S3Service s3Service;
    private final EntityManager entityManager;
    private final String ROLE_COMPANY = "ROLE_COMPANY";
    private final String ROLE_ADMIN = "ROLD_ADMIN";

    @Override
    public Long createSettlementRequest(MultipartFile file, SettlementRequestDto dto, CustomUserDetails member) {
        if (member.getAuthorities().stream().noneMatch(au -> au.getAuthority().equals(ROLE_COMPANY))) {
            return -5L;
        }

        Funding funding = fundingRepository.findById(dto.getFundingId())
                .orElse(null);
        if (funding == null) {
            return -4L;
        }
        if (!member.getId().equals(funding.getEntId())) {
            return -3L;
        }
        if (!funding.getProgressStatus().equals(FundingProgressStatus.PENDING_SETTLEMENT)) {
            return -2L;
        }

        try {
            dto.setSettlementFile(s3Service.upload(file, funding.getName()));
        } catch (IOException e) {
            return -1L;
        }

        Settlement settlement = settlementRepository.save(Settlement.builder()
                .entId(member.getId())
                .fundingId(funding.getId())
                .settlementPrice(dto.getSettlementPrice())
                .returnRate(dto.getReturnRate())
                .depositeDate(dto.getDepositeDate())
                .settlementFile(dto.getSettlementFile())
                .status(SettlementStatus.REQUEST)
                .build());
        if (!entityManager.contains(settlement)) {
            return 0L;
        }

        return settlement.getId();
    }

    @Override
    public List<SettlementListItemDto> getSettlementRequestList(CustomUserDetails member) {
        if (member.getAuthorities().stream().noneMatch(au -> au.getAuthority().equals(ROLE_ADMIN))) {
            return null;
        }

        return settlementRepository.getSettlementList(SettlementStatus.REQUEST.name());
    }

    @Override
    public SettlementResponseDto getSettlement(Long settlementId, CustomUserDetails member) {
        Settlement settlement = settlementRepository.findById(settlementId)
                .orElse(null);
        if (settlement == null ||
                (member.getAuthorities().stream().noneMatch(au -> au.getAuthority().equals(ROLE_ADMIN)) &&
                        !settlement.getId().equals(member.getId()))
        ) {
            return null;
        }

        return new SettlementResponseDto(settlement);
    }

    @Override
    public int updateSettlementStatus(Long settlementId, String status, CustomUserDetails member) {
        if (member.getAuthorities().stream().noneMatch(au -> au.getAuthority().equals(ROLE_ADMIN))) {
            return -3;
        }

        Settlement settlement = settlementRepository.findById(settlementId)
                .orElse(null);
        if (settlement == null) {
            return -2;
        }

        if (Stream.of(SettlementStatus.values())
                .noneMatch(sts -> sts.name().equals(status))) {
            return -1;
        }

        settlement.updateStatus(SettlementStatus.valueOf(status.toUpperCase(Locale.ROOT)));

        return 1;
    }

    @Override
    public int deleteSettlement(Long settlementId, CustomUserDetails member) {
        if (member.getAuthorities().stream().noneMatch(au -> au.getAuthority().equals(ROLE_ADMIN))) {
            return -2;
        }

        Settlement settlement = settlementRepository.findById(settlementId)
                .orElse(null);
        if (settlement == null) {
            return -1;
        }

        settlementRepository.delete(settlement);
        return 1;
    }
}