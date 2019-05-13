import com.fazecast.jSerialComm.SerialPort;

import java.io.PrintWriter;

public class ArduinoConnectie {

    public SerialPort comPort;

    public ArduinoConnectie(int baudRate, int port) //commit
    {
        try {
            comPort = SerialPort.getCommPorts()[port];
            comPort.openPort();
            comPort.setBaudRate(baudRate);
        }catch (Exception e ){
            e.printStackTrace();
        }
    }

    public void writeString(String s){
        comPort.writeBytes(s.getBytes(), s.length());
    }

    public void writeStringKlaas(String s){
        PrintWriter pout = new PrintWriter(comPort.getOutputStream());
        pout.print(s);
        pout.flush();

    }

    public String readString() {
        String msg = "";
        try {
            while (comPort.bytesAvailable() == 0) {
                Thread.sleep(20);
            }
            byte[] readBuffer = new byte[comPort.bytesAvailable()];         //make buffer array as large as data sent and fill with data
            int numRead = comPort.readBytes(readBuffer, readBuffer.length); //measure readBuffer array and save size in an int
            msg = new String(readBuffer);                                   //convert readBuffer to string
        } catch (Exception e){
            e.printStackTrace();
        }
        return msg;
    }

    public void closeConnectie(int port)
    {
        comPort = SerialPort.getCommPorts()[port];
        comPort.closePort();
    }

    public void openConnectie(int baudRate, int port)
    {
        comPort = SerialPort.getCommPorts()[port];
        comPort.openPort();
        comPort.setBaudRate(baudRate);
    }

}
