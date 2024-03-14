package com.ssafy.artchain.market.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "market")
public class Market {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "funding_id", nullable = false)
    private Long fundingId;

    @Column(name = "contract_address", nullable = false)
    private String contractAddress;

    @Column(name = "piece_name", nullable = false)
    private String pieceName;

    @Column(name = "piece_count", nullable = false)
    private Long pieceCount;

    @Column(name = "total_coin", nullable = false)
    private Long totalCoin;

    @Column(name = "coin_per_piece", precision = 19, scale = 2, nullable = false)
    private BigDecimal coinPerPiece;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "transaction_hash")
    private String transactionHash;

    @Column(name = "transaction_time")
    private LocalDateTime transactionTime;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(name = "buyer_id")
    private Long buyerId;
}