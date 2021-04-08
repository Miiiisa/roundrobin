
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class roundRobin {
    public static Queue<c1> q1 = new ArrayDeque();
    public static List<c1> l1 = new ArrayList();


    public static void main(String[] args) throws IOException {
        int timer = 3;
        //input and output
        Scanner scan = new Scanner(System.in);
        System.out.print("Please input file name: ");
        String fileName = scan.nextLine();
        System.out.print("Please input quantum: ");
        timer = scan.nextInt();
        scheduler(fileName,timer);
        double avgWait = 0;
        double avgTurnAround = 0;
        for (c1 process: l1){
            avgTurnAround += process.turnAroundTime;
            avgWait += process.waitingTime;
            System.out.println("Process " + process.processName + "  Arrival Time: " + process.arrivalTime + "  Burst Time: " + process.burstTime + "  Waiting Time: " + process.waitingTime + "  TurnAround Time: " + process.turnAroundTime);
        }
        avgTurnAround /= l1.size();
        avgWait /= l1.size();
        System.out.println("Average Waiting Time: " + avgWait);
        System.out.println("Average Turn Around Time: " + avgTurnAround);
    }

    //store data into an array
    public static List<c1> readFile(String fileName) throws IOException {
        ArrayList<c1> list = new ArrayList();
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = br.readLine())!= null){
            if (line.startsWith("#")){
                continue;
            }
            String[] splits = line.split("\\s+");
            c1 process = new c1(splits[0], Integer.parseInt(splits[1]), Integer.parseInt(splits[2]));
            list.add(process);
        }
        return list;
    }

    public static void scheduler(String fileName, int timer) throws IOException {
        List<c1> processList = readFile(fileName);
        Collections.sort(processList);
        int n_process = processList.size();
        int time = 0;
        c1 insert = null;
        while (l1.size() != n_process){
            for (int i = 0; i<processList.size(); i++){
                c1 process = processList.get(i);
                if (process != null && process.arrivalTime <= time){
                    processList.set(i, null);
                    q1.offer(process);
                }
            }
            if (insert != null){
                q1.offer(insert);
            }
            c1 curProcess = q1.poll();
            if (curProcess == null){
                time ++;
                continue;
            }
            if (curProcess.quantum <= timer){
                time += curProcess.quantum;
                curProcess.quantum = 0;
                curProcess.endTime = time;
                curProcess.turnAroundTime = curProcess.endTime - curProcess.arrivalTime;
                curProcess.waitingTime = curProcess.turnAroundTime - curProcess.burstTime;
                l1.add(curProcess);
                insert = null;
            }
            else{
                time += timer;
                curProcess.quantum -= timer;
                insert = curProcess;
            }
        }
    }
}

class c1 implements Comparable<c1>{
    String processName;
    int arrivalTime;
    int burstTime;
    int quantum;
    int endTime;
    int waitingTime;
    int turnAroundTime;

    /**
     * @param processName name
     * @param arrivalTime arrivalTime
     * @param burstTime burstTime
     */
    public c1(String processName, int arrivalTime, int burstTime) {
        this.processName = processName;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.quantum = burstTime;
    }

    @Override
    public int compareTo(c1 o) {
        if (this.arrivalTime > o.arrivalTime){
            return 1;
        }
        else if (this.arrivalTime < o.arrivalTime){
            return -1;
        }
        else
            return 0;
    }

    @Override
    public String toString() {
        return "Process{" +
                "ProcessName='" + processName + '\'' +
                ", ArrivalTime=" + arrivalTime +
                ", BurstTime=" + burstTime +
                ", Quantum=" + quantum +
                ", EndTime=" + endTime +
                ", WaitingTime=" + waitingTime +
                ", TurnAroundTime=" + turnAroundTime +
                '}';
    }
}
