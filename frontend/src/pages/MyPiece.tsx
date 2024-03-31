import { Box } from "@chakra-ui/react";
import { useState, useEffect } from "react";
import { MypieceTopNav } from "../components/Mypage/Piece/MypieceTopNav";
import { MyInvest } from "../components/Mypage/Piece/MyInvest/MyInvest";
import { MyTransactionList } from "../components/Mypage/Piece/MyTransaction/MyTransactionList";
import { MyTransaction } from "../components/Mypage/Piece/MyTransaction/MyTransaction";

interface Props {
  type: string;
}

export const MyPiece = ({ type }: Props) => {
  const [check, setCheck] = useState(type);

  const handleCheck = (whatCheck: string) => {
    setCheck(whatCheck);
  };

  return (
    <>
      <Box position={"sticky"} top={0}>
        <MypieceTopNav onCheck={handleCheck} check={check} />
      </Box>
      <Box px={"6%"} mt={5} overflowY={"auto"} height={"auto"}>
        {check === "invest" ? <MyInvest /> : <MyTransaction />}
      </Box>
    </>
  );
};
