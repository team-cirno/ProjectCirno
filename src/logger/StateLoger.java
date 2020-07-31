package logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import java.util.ArrayList;

public class StateLoger {

    ArrayList<Pair> tmp;

    private class Pair<T, T1> {
        String date;
        Integer data;

        public Pair(String date, Integer data) {
            this.date = date;
            this.data = data;
        }

        public void increment(){
            this.data += 1;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Integer getData() {
            return data;
        }

        public void setData(Integer data) {
            this.data = data;
        }
    }

}
