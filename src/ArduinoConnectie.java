import com.fazecast.jSerialComm.SerialPort;

public class ArduinoConnectie { // Klasse om de arduino connectie via JSerialComm makkelijker te maken.

    // Serial port om te gebruiken.
    public SerialPort comPort;

    public ArduinoConnectie(int baudRate, int port) //Constructor die automatisch de connectie al opent.
    {
        try {
            comPort = SerialPort.getCommPorts()[port];
            comPort.openPort();
            comPort.setBaudRate(baudRate);
        }catch (Exception e ){
            e.printStackTrace();
        }
    }

    public void writeString(String s){ // Schrijf een string naar de arduino's serial.
        comPort.writeBytes(s.getBytes(), s.length());
    }


    public String readString() { // Lees van de serial.
        String msg = "";
        try {
            while (comPort.bytesAvailable() == 0) {
                Thread.sleep(20);
            }
            byte[] readBuffer = new byte[comPort.bytesAvailable()];
            int numRead = comPort.readBytes(readBuffer, readBuffer.length);
            msg = new String(readBuffer);
        } catch (Exception e){
            e.printStackTrace();
        }
        return msg;
    }

    public void closeConnectie(int port) // Sluit de connectie.
    {
        comPort = SerialPort.getCommPorts()[port];
        comPort.closePort();
    }

    public void openConnectie(int baudRate, int port) // Open de connectie.
    {
        comPort = SerialPort.getCommPorts()[port];
        comPort.openPort();
        comPort.setBaudRate(baudRate);
    }

}
