import { useNavigate } from "react-router-dom";
import styled from "styled-components";

const LeftStyled = styled.div`
  display: flex;
  width: 50%;
  height: 100%;
  justify-content: left;
  align-items: center;
  margin-left: 30px;
  img {
    width: 40px;
    margin-right: 5px;
    cursor: pointer;
    @media (max-width: 768px) {
      width: 30px;
    }
  }
  div {
    cursor: pointer;
    display: flex;
    font-size: 22px;
    font-family: "Binggrae-Two";
    @media (max-width: 768px) {
      font-size: 15px;
      margin-top: 2px;
    }
  }
  @media (max-width: 768px) {
    margin-left: 5px;
    width: auto;
  }
`;

const RightStyled = styled.div`
  display: flex;
  margin-right: 40px;
  width: 400px;
  height: 100%;
  justify-content: right;
  align-items: center;
  @media (max-width: 768px) {
    width: 70vw;
    margin-right: 5px;
    font-size: 12px;
  }
  div {
    display: flex;
    width: 13%;
    height: 100%;
    justify-content: center;
    align-items: center;
    font-size: 15px;
    @media (max-width: 768px) {
      font-size: 11px;
    }
    &:hover {
      font-weight: 600;
      cursor: pointer;
    }
    &:first-child {
      font-weight: ${(props) => props.nowpage === "profile" && "700"};
    }
    &:nth-child(2) {
      font-weight: ${(props) => props.nowpage === "mission" && "700"};
    }
    &:nth-child(3) {
      font-weight: ${(props) => props.nowpage === "feedback" && "700"};
    }
    &:nth-child(4) {
      font-weight: ${(props) => props.nowpage === "vault" && "700"};
    }
    &:nth-child(5) {
      font-weight: ${(props) => props.nowpage === "statistics" && "700"};
    }
    &:nth-child(6) {
      font-weight: ${(props) => props.nowpage === "ranking" && "700"};
    }
    &:nth-child(7) {
      width: 15%;
    }
  }
`;

const MyHeader = ({ nowpage }) => {
  const isLoggedIn = localStorage.getItem("isLoggedIn");
  const navigate = useNavigate();

  const nameClickHandler = (e) => {
    e.preventDefault();
    navigate("/");
  };

  const profileClickHandler = (e) => {
    e.preventDefault();
    navigate("/profile");
  };
  const statisticsClickHandler = (e) => {
    e.preventDefault();
    navigate("/statistics");
  };
  const missionClickHandler = (e) => {
    e.preventDefault();
    navigate("/mission");
  };
  const feedbackClickHandler = (e) => {
    e.preventDefault();
    navigate("/feedback");
  };
  const vaultClickHandler = (e) => {
    e.preventDefault();
    navigate("/vault");
  };

  const rankingClickHandler = (e) => {
    e.preventDefault();
    navigate("/ranking");
  };
  const logoutClickHandler = (e) => {
    e.preventDefault();
    if (isLoggedIn === "true") {
      localStorage.setItem("isLoggedIn", "false");
      navigate("/");
    } else {
      navigate("/login");
    }
  };
  return (
    <div className="MyHeader">
      <LeftStyled>
        <img
          onClick={nameClickHandler}
          className="logo"
          src="/assets/logo.png"
        />
        <div onClick={nameClickHandler} className="service_name">
          ₩alletty
        </div>
      </LeftStyled>
      <RightStyled nowpage={`${nowpage}`}>
        <div onClick={profileClickHandler}>프로필</div>
        <div onClick={missionClickHandler}>미션</div>
        <div onClick={feedbackClickHandler}>피드백</div>
        <div onClick={vaultClickHandler}>금고</div>
        <div onClick={statisticsClickHandler}>통계</div>
        <div onClick={rankingClickHandler}>랭킹</div>
        <div onClick={logoutClickHandler}>
          {isLoggedIn === "true" ? "로그아웃" : "로그인"}
        </div>
      </RightStyled>
    </div>
  );
};

export default MyHeader;
