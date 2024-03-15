// SPDX-License-Identifier: MIT
// OpenZeppelin Contracts (last updated v5.0.0) (token/ERC20/ERC20.sol)

pragma solidity ^0.8.20;

import "@openzeppelin/contracts/token/ERC20/ERC20.sol";

// 아트코인 민팅
contract ArtcoinContract is ERC20 {
    // 컨트랙트 생성 > 1개 만들고 이후에 1개 소각
    constructor() ERC20("ArtCoin", "ART") {
        _mint(msg.sender, 1 * 10**18); 
    }

    // 토큰 민트
    function mintTokens(uint256 _Supply) public {
        _mint(msg.sender, _Supply * 10**18);
    }

    // 토큰 소각
    function burnTokens(uint256 burnAmount) public {
        _burn(msg.sender, burnAmount * 10 ** 18);
    }

    // 토큰 전송
    function transferToken(address to, uint256 amount) public {
        _transfer(msg.sender, to, amount * 10 ** 18);
    }
}

