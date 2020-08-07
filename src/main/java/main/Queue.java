package main;

import java.util.Arrays;

public class Queue {
    private Node head = null;
    private Node tail = null;
    private int count = 0;
    private int limit = 30;

    public Queue(int limit) {
        this.limit = limit;
    }

    public int getCount() {
        return count;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void enqueue(double value){
        if (count>29){
            dequeue();
        }
        Node n = new Node(value);
        if(count==0){
            head = n;
            tail = n;
        }else{
            tail.setNext(n);
            tail=n;
        }
        count+=1;
    }

    public void dequeue(){
        if (count>0){
            head = head.getNext();
            count-=1;
            if (count == 0){
                tail=null;
            }
        }
    }

    public String toArray(){
        Node cur = head;
        double[] output = new double[limit];
        int c = 0;
        while (!(cur == null)){
            output[c]=cur.getValue();
            c+=1;
            cur=cur.getNext();
        }
        return Arrays.toString(output);
    }
}
