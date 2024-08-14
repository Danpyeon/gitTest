package teamProject;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

// DrawingServer 클래스는 클라이언트 간의 그림 데이터를 주고받을 수 있는 서버를 구현합니다.
public class MultiServer {
    // ConcurrentHashMap은 클라이언트 소켓과 ObjectOutputStream을 관리합니다.
    private static ConcurrentHashMap<Socket, ObjectOutputStream> clients = new ConcurrentHashMap<>();
    // ExecutorService는 최대 20개의 스레드 풀을 관리합니다.
    private static ExecutorService pool = Executors.newFixedThreadPool(20);

    // 메인 메서드는 서버를 시작하고 클라이언트 연결을 수락합니다.
    public static void main(String[] args) throws Exception {
        // 서버 소켓을 포트 5000에서 생성합니다.
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Drawing server is running...");

        // 무한 루프로 클라이언트 연결을 대기합니다.
        while (true) {
            try {
                // 클라이언트 연결을 수락합니다.
                Socket clientSocket = serverSocket.accept();
                // 클라이언트에 데이터를 전송하기 위한 ObjectOutputStream을 생성하고 초기화합니다.
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                oos.flush();
                // 클라이언트 소켓과 ObjectOutputStream을 맵에 추가합니다.
                clients.put(clientSocket, oos);
                // 클라이언트 핸들러를 스레드 풀에 제출하여 실행합니다.
                pool.execute(new ClientHandler(clientSocket, oos));
            } catch (IOException e) {
                // 연결 수락 중 에러가 발생하면 메시지를 출력합니다.
                System.out.println("Error accepting connection: " + e.getMessage());
            }
        }
    }

    // ClientHandler 클래스는 각 클라이언트 연결을 처리합니다.
    private static class ClientHandler implements Runnable {
        private Socket socket;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;

        // 생성자는 클라이언트 소켓과 ObjectOutputStream을 받아들입니다.
        ClientHandler(Socket socket, ObjectOutputStream oos) {
            this.socket = socket;
            this.oos = oos;
            try {
                // 클라이언트로부터 데이터를 읽기 위한 ObjectInputStream을 생성합니다.
                this.ois = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                // 스트림 설정 중 에러가 발생하면 메시지를 출력합니다.
                System.out.println("Error setting up stream reader: " + e.getMessage());
            }
        }

        @Override
        public void run() {
            try {
                Object inputObject;
                // 클라이언트로부터 데이터를 계속 읽어옵니다.
                while ((inputObject = ois.readObject()) != null) {
                    // 데이터를 DataPost 타입으로 변환합니다.
                    DataPost dp = (DataPost) inputObject;
                    if (inputObject instanceof DataPost) {
                        // 받은 데이터를 다시 DataPost 타입으로 변환합니다.
                        DataPost receivedData = (DataPost) inputObject;
                        // 모든 클라이언트에게 데이터를 브로드캐스트합니다.
                        broadcast(receivedData);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                // 데이터 읽기 중 에러가 발생하면 메시지를 출력합니다.
                System.out.println("Error reading from client: " + e.getMessage());
            } finally {
                // 클라이언트 연결이 종료되면 맵에서 제거하고 자원을 해제합니다.
                clients.remove(socket);
                try {
                    ois.close();
                    oos.close();
                    socket.close();
                } catch (IOException e) {
                    // 자원 해제 중 에러가 발생하면 메시지를 출력합니다.
                    System.out.println("Error closing resources: " + e.getMessage());
                }
            }
        }

        // broadcast 메서드는 모든 클라이언트에게 데이터를 전송합니다.
        private void broadcast(DataPost data) throws IOException {
            for (ObjectOutputStream client : clients.values()) {
                client.writeObject(data);
                client.flush();
            }
        }
    }
}
