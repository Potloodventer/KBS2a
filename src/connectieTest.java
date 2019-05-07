import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class connectieTest {

    public static void main(String[] args)
    {
        //arduino
        ArduinoConnectie arduinoConnectie = new ArduinoConnectie(9600, 0);
        arduinoConnectie.writeString("aan");
        arduinoConnectie.readString();



    }




}




