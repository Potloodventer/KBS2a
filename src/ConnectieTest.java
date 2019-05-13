import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class ConnectieTest {

    public static void main(String[] args)
    {

        for(SerialPort x : SerialPort.getCommPorts()){
            System.out.print(x.toString());
        }



    }




}




