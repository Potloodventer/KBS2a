public class Main {
    public static void main(String[] args) {

        HoofdschermGUI hoofdScherm = new HoofdschermGUI();
        HMIStatusGUI hmiStatusGUI = new HMIStatusGUI(new ArduinoConnectie(1, 1),
                new ArduinoConnectie(1,1));

         BPPResultaatGUI bppResultaatGUI = new BPPResultaatGUI(hmiStatusGUI);

         //bppResultaatGUI.printArrays();

//
//        for (int i = 0; i < 50; i++) {
//            bppResultaatGUI.generateNumber();
//        }
    }
}
