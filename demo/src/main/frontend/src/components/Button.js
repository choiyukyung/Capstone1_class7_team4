const Button = ({ text, type, onClick }) => {
  return (
    <button
      className={["button", `button_${type}`].join(" ")}
      onClick={onClick}
    >
      {text}
    </button>
  );
};

export default Button;
