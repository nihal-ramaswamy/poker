import {
  View,
  Text,
  Dimensions,
  SafeAreaView,
  StyleSheet,
  TouchableOpacity,
} from "react-native";
import { Slider } from "@miblanchard/react-native-slider";
import { useState } from "react";
const { height } = Dimensions.get("screen");

const moneyViewHeight = height * 0.082;
const buttonViewHeight = height * 0.272;
const gameButtonViewHeight = buttonViewHeight / 2;
const sliderViewHeight = buttonViewHeight / 2;
const cardViewHeight = height - moneyViewHeight - buttonViewHeight;

const MoneyView = () => {
  return (
    <View className="mt-7 bg-gray-800 w-full" style={styles.moneyView}></View>
  );
};

const CardView = () => {
  return <View className="bg-blue-800" style={styles.cardView}></View>;
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
});

export default GameScreen;
