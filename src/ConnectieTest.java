//import com.fazecast.jSerialComm.SerialPort;
//import com.fazecast.jSerialComm.SerialPortDataListener;
//import com.fazecast.jSerialComm.SerialPortEvent;
//
//public class ConnectieTest {
//
//    public static void main(String[] args)
//    {
//        //arduino
//        ArduinoConnectie arduinoConnectie = new ArduinoConnectie(9600, 0);
//        arduinoConnectie.writeString("aan");
//
//
//
//        arduinoConnectie.comPort.addDataListener(new SerialPortDataListener() {
//            @Override
//            public int getListeningEvents() {
//                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
//            }
//
//            @Override
//            public void serialEvent(SerialPortEvent serialPortEvent) {
//                arduinoConnectie.readString();
//            }
//        });
//
//
//
//    }
//
//


//}




