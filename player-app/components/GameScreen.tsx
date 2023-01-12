import { Slider } from "@miblanchard/react-native-slider";
import { useAssets } from "expo-asset";
import { useState } from "react";
import {
  Dimensions,
  ImageBackground,
  SafeAreaView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from "react-native";
import MoneyPotSVG from "./svgs/MoneyPotSVG";
import PokerChipSVG from "./svgs/PokerChipSVG";

const { height } = Dimensions.get("screen");
const moneyViewHeight = height * 0.082;
const buttonViewHeight = height * 0.272;
const gameButtonViewHeight = buttonViewHeight / 2;
const sliderViewHeight = buttonViewHeight / 2;
const cardViewHeight = height - moneyViewHeight - buttonViewHeight;

const MoneyView = () => (
  <View
    className="mt-7 bg-gray-800 w-full px-6 flex flex-row justify-between items-center"
    style={styles.moneyView}
  >
    <View className="flex flex-row items-center">
      <PokerChipSVG />
      <Text className="ml-2 text-white text-xl font-bold">20000</Text>
    </View>
    <View className="flex flex-row items-center">
      <MoneyPotSVG />
      <Text className="ml-2 text-white text-xl font-bold">200</Text>
    </View>
  </View>
);

const CardView = () => {
  const [assets, error] = useAssets([require("../assets/poker-board-bg.jpeg")]);
  if (error || assets === undefined) return null;

  const image = assets[0];
  return <ImageBackground source={image} style={styles.cardView} />;
};

const ButtonView = () => {
  const [value, setValue] = useState<number>(0);
  return (
    <View className="bg-gray-800 w-full" style={styles.buttonView}>
      <View
        className="flex flex-row justify-evenly items-center pt-2"
        style={styles.gameButtonView}
      >
        <TouchableOpacity className="w-1/4 h-2/4 rounded-lg flex justify-center items-center bg-blue-800 p-2">
          <Text className="text-white text-lg font-bold">Call 200</Text>
        </TouchableOpacity>
        <TouchableOpacity className="w-1/4 h-2/4 rounded-lg flex justify-center items-center bg-blue-800 p-2">
          <Text className="text-white text-lg font-bold">Raise</Text>
        </TouchableOpacity>
        <TouchableOpacity className="w-1/4 h-2/4 rounded-lg flex justify-center items-center bg-blue-800 p-2">
          <Text className="text-white text-lg font-bold">Fold</Text>
        </TouchableOpacity>
      </View>
      <SafeAreaView className="mx-5" style={styles.sliderView}>
        <Slider
          step={100}
          maximumValue={10000}
          renderAboveThumbComponent={() => (
            <View>
              <Text className="text-white">{value}</Text>
            </View>
          )}
          thumbStyle={styles.sliderThumb}
          minimumTrackTintColor="#003399"
          value={value}
          onValueChange={(value) => setValue(value)}
        />
      </SafeAreaView>
    </View>
  );
};

const GameScreen = () => {
  return (
    <SafeAreaView>
      <MoneyView />
      <CardView />
      <ButtonView />
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  moneyView: {
    height: moneyViewHeight,
  },
  cardView: {
    height: cardViewHeight,
  },
  buttonView: {
    height: buttonViewHeight,
  },
  gameButtonView: {
    height: gameButtonViewHeight,
  },
  sliderView: {
    height: sliderViewHeight,
  },
  sliderThumb: {
    backgroundColor: "#FFFFFF",
    borderColor: "#003399",
    borderRadius: 30 / 2,
    borderWidth: 2,
    height: 30,
    width: 30,
  },
  sliderTrack: {
    backgroundColor: "#003399",
    borderRadius: 2,
    height: 4,
  },
});

export default GameScreen;
