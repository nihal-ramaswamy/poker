import React from "react";

const MessageBox: React.FC<{ text: string }> = ({ text }) => (
  <div className="bg-blue-400 my-2 p-2 rounded-md text-white">{text}</div>
);

const GameView: React.FC<{}> = () => {
  return (
    <div className="h-screen w-screen flex flex-col bg-gray-800 text-white">
      <div className="w-full h-1/5 flex justify-center items-center">
        <div className="text-2xl">Pot Amount: 2000</div>
      </div>
      <div className="w-full h-3/5 bg-blue-700"></div>
      <div className="w-full h-1/5 flex justify-center items-center">
        <div className="w-1/2 p-5 grid grid-cols-2 gap-4">
          <button className="p-2 rounded-md bg-blue-500">Settings</button>
          <button className="p-2 rounded-md bg-blue-500">Settings</button>
          <button className="p-2 rounded-md bg-blue-500">Settings</button>
          <button className="p-2 rounded-md bg-blue-500">Settings</button>
        </div>
        <div className="w-1/2 h-full p-5">
          <div className="bg-white h-full text-black rounded-md p-2 overflow-y-scroll">
            <MessageBox text="lmao" />
            <MessageBox text="Vinay raised 200." />
            <MessageBox text="Anisha called 200" />
            <MessageBox text="Prithvi won 500 with a hand of twin pairs. Two jacks and two 5s." />
            <MessageBox text="Prithvi won 500 with a hand of twin pairs. Two jacks and two 5s." />
            <MessageBox text="Prithvi won 500 with a hand of twin pairs. Two jacks and two 5s." />
            <MessageBox text="Prithvi won 500 with a hand of twin pairs. Two jacks and two 5s." />
          </div>
        </div>
      </div>
    </div>
  );
};

export default GameView;
