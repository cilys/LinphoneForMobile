package com.cilys.linphoneformobile.utils;

public class TimeUtils {
    public static String fomcatTimeToSecond(long count){
        if (count >= 0) {
            if (count < 60) {
                return "00:" +  fomcat(count);
            } else {
                long min = count / 60;

                if (min < 10) {
                    return "0" + min + ":" + fomcat(count % 60);
                } else if (min > 60) {
                    long hour = min / 60;

                    return hour + ":" + fomcat(min % 60) + ":" + fomcat(count % 60);
                }
                return min + ":" + (count % 60);
            }
        } else {
            return "00:00";
        }
    }

    private static String fomcat(long count){
        if (count < 10){
            return "0" + count;
        } else {
            return String.valueOf(count);
        }
    }

//    public static void main(String[] args) {
//        System.out.println(fomcatTimeToSecond(0));
//        System.out.println(fomcatTimeToSecond(9));
//        System.out.println(fomcatTimeToSecond(99));
//        System.out.println(fomcatTimeToSecond(999));
//        System.out.println(fomcatTimeToSecond(9999));
//        System.out.println(fomcatTimeToSecond(99999));
//    }
}
