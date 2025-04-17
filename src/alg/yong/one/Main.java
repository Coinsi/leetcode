package alg.yong.one;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine().trim();
        int index = line.lastIndexOf("],");
        String strs = line.substring(0, index+1).replaceAll("[\\[\\]]", "");
        String kstring = line.substring(index+2).trim();
//        System.out.println(strs);
//        System.out.println(kstring);
        String[] tokens = strs.split(",");
//        System.out.println(Arrays.toString(tokens));
        int[] songs = Arrays.stream(tokens).map(String::trim).mapToInt(Integer::parseInt).toArray();
        int k = Integer.parseInt(kstring);
//        System.out.println(Arrays.toString(songs));
//        System.out.println(k);


        int[] result = favoriteSongs(songs, k);
        System.out.println(Arrays.toString(result));
    }

    public static int[] favoriteSongs(int[] songs, int k) {

        Map<Integer, Integer> freeMap = new HashMap<>();
        for(int song : songs){
            freeMap.put(song, freeMap.getOrDefault(song, 0) + 1);

        }
        PriorityQueue<Map.Entry<Integer, Integer>> minHeap = new PriorityQueue<>(
                (a, b) -> a.getValue().equals(b.getValue()) ? b.getKey().compareTo(a.getKey()) : a.getValue().compareTo(b.getValue())
        );;
        for(Map.Entry<Integer, Integer> entry : freeMap.entrySet()){
            minHeap.offer(entry);
            if(minHeap.size() > k){
                minHeap.poll();
            }
        }

       List<Integer> result = new ArrayList<>();
        while(!minHeap.isEmpty()){
            result.add(minHeap.poll().getKey());
        }
        Collections.sort(result);

        return result.stream().mapToInt(Integer::intValue).toArray();

    }
}
