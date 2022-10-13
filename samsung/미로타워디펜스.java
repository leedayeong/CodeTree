package samsung;

import java.io.*;
import java.util.*;

/*
5 1
1 0 0 4 2
2 1 3 2 1
0 0 0 2 5
1 0 0 0 3
1 2 1 3 3
1 3

32
 */
public class 미로타워디펜스{
	
	static int N, M;
	static int sum;
	
	static int num, numCnt, lineCnt;

	static int[][] map;
	static Node[] attacks;
	
	static List<Integer> list; 
	
	static int[] dy = {0, 1, 0, -1}; // 우 하 좌 상
	static int[] dx = {1, 0, -1, 0};
	
	static int[] wy = {0, 1, 0, -1}; // 좌 하 우 상
	static int[] wx = {-1, 0, 1, 0};
	
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        sum = 0;
        
        map = new int[N][N];
        attacks = new Node[M];
        
        for (int i = 0; i < N; i++) {
        	st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
        
        for (int i = 0; i < M; i++) {
        	st = new StringTokenizer(br.readLine());
        	attacks[i] = new Node(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
		}
//        for (int i = 0; i < N; i++) {
//			for (int j = 0; j < N; j++) {
//				System.out.print(map[i][j] + " ");
//			}
//			System.out.println();
//		}
        for (int t = 0; t < M; t++) {
//			System.out.println("before one");
//			for (int i = 0; i < N; i++) {
//				for (int j = 0; j < N; j++) {
//					System.out.print(map[i][j] + " ");
//				}
//				System.out.println();
//			}
			attack(t);
			two();
//			System.out.println("after two");
//			for (Integer string : list) {
//				System.out.print(string + " ");
//			}
//			System.out.println();
//			three();
			while(true) {
				if(!three()) break;
			}
			list = four();
//			System.out.println("after four");
//			for (Integer string : list) {
//				System.out.print(string + " ");
//			}
//			System.out.println();
//			System.out.println("after five");
			five();
//			System.out.println();
//			System.out.println("t : " + t + ", sum  : " + sum);
		}
        System.out.println(sum);
        
       
        
    }
    public static void attack(int k) {
    	int y = N/2;
    	int x = N/2;
    	
    	for (int t = 1; t <= attacks[k].p; t++) {
			int ny = y + dy[attacks[k].d] * t;
			int nx = x + dx[attacks[k].d] * t;
			
			sum += map[ny][nx];
			
			map[ny][nx] = 0;
		}
    }
    
    public static void two() {
    	list = new LinkedList<>();
    	
    	int y = N/2;
    	int x = N/2-1;
    	
    	if(map[y][x]!= 0)list.add(map[y][x]);
    	
    	num = 1;
    	numCnt = 0;
    	lineCnt = 1;
    	
    	while(true) {
    		if(num == N-1 && lineCnt == 3) break;
    		
    		int dir = 0;
    		if(num %2 == 1 && lineCnt == 0) dir = 0;
    		else if(num %2 == 1 && lineCnt == 1) dir = 1;
    		else if(num %2 == 0 && lineCnt == 0) dir = 2;
    		else if(num %2 == 0 && lineCnt == 1) dir = 3;
    		
    		int ny = y + wy[dir];
    		int nx = x + wx[dir];
    		y = ny;
    		x = nx;
    		if(map[y][x]!= 0) list.add(map[y][x]);
    		
    		if(++numCnt == num) {
    			numCnt = 0;
    			lineCnt++;
    			if(num != N-1 && lineCnt == 2) {
    				lineCnt = 0;
    				num++;
    			}
    		}
    	}
    }
    
    
    public static boolean three() {
    	boolean chk = false;
    	
    	int len = list.size();
    	
    	boolean[] remove = new boolean[len];
    	
    	int beforeIdx = 0;
    	int before = list.get(0);
    	int current = 0;
    	for (int i = 1; i < len+1; i++) {
    		if(i == len) current = 0;
    		else current = list.get(i);
    		if(before != current) {
    			int cnt = i - beforeIdx;
    			if(cnt >=4 ) {
    				chk = true;
    				for (int j = beforeIdx; j < i ; j++) {
						remove[j] = true;
					}
    			}
    			before = current;
    			beforeIdx = i;
    		}
		}
    	

    	for (int i = len-1; i >= 0; i--) {
			if(remove[i]) {
				sum += list.get(i);
				list.remove(i);
			}
		}
    	
    	
    	return chk;
    }
    
    public static List<Integer> four() {
    	
    	int len = list.size();
    	
    	List<Integer> tmpList = new LinkedList<Integer>();
    	
    	int beforeIdx = 0;
    	int before = list.get(0);
    	
    	int current = 0;
    	int cnt = 0;
    	
    	for (int i = 1; i < len + 1; i++) {
    		if(i == len) current = 0;
    		else current = list.get(i);
    		
    		if(before != current) {
    			cnt = i - beforeIdx;
    			
    			tmpList.add(cnt);
				tmpList.add(before);
				
    			before = current;
    			beforeIdx = i;
    		}
		}
    	
    	
    	return tmpList;
    }
    
    public static void five() {
    	
    	for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				map[i][j] = 0;
			}
		}
    	
    	int y = N/2;
    	int x = N/2-1;
    	
    	map[y][x] = list.get(0);
    	
    	int len = list.size();
    	int idx = 1;
    	num = 1;
    	numCnt = 0;
    	lineCnt = 1;
    	
    	while(true) {
    		if(num == N-1 && lineCnt == 3) break;
    		
    		int dir = 0;
    		if(num %2 == 1 && lineCnt == 0) dir = 0;
    		else if(num %2 == 1 && lineCnt == 1) dir = 1;
    		else if(num %2 == 0 && lineCnt == 0) dir = 2;
    		else if(num %2 == 0 && lineCnt == 1) dir = 3;
    		
    		int ny = y + wy[dir];
    		int nx = x + wx[dir];
    		y = ny;
    		x = nx;
    		
    		map[y][x] = list.get(idx++);
    		
    		if(len == idx) break;
    		
    		if(++numCnt == num) {
    			numCnt = 0;
    			lineCnt++;
    			if(num != N-1 && lineCnt == 2) {
    				lineCnt = 0;
    				num++;
    			}
    		}
    	}
    	
//    	for (int i = 0; i < N; i++) {
//			for (int j = 0; j < N; j++) {
//				System.out.print(map[i][j] + " ");
//			}
//			System.out.println();
//		}
    }
    
    public static class Node{
    	byte d;
    	byte p;
    	public Node(int d, int p) {
    		this.d = (byte) d;
    		this.p = (byte) p;
    	}
    }
   

}