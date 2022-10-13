package samsung;

import java.io.*;
import java.util.*;

/*
4 3
1 2 4 4
4 2 2 2
5 2 6 6
5 3 3 1

1
 */
public class 냉방시스템{
	
	static int N;
	static int M;
	static int K;
	
	static List<Desk> desks = new ArrayList<>();
	static List<Aircon> aircons = new ArrayList<>();
	static Queue<Aircon> queue = new LinkedList<>();
	
	static Node[][] map;
	static int[][] sum;
	static int[][] sumTmp;
	
	static int[] dy = {0, -1, 0, 1}; // 좌상우하
	static int[] dx = {-1, 0, 1, 0};
	
	static int[] wy = {-1, -1, 0, 1, 1, 1, 0, -1}; // 상 우상 우 우하 하 좌하 좌 좌상
	static int[] wx = {0, 1, 1, 1, 0, -1, -1, -1};
	
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        N = Integer.parseInt(st.nextToken()) + 1;
        M = Integer.parseInt(st.nextToken()) + 1;
        K = Integer.parseInt(st.nextToken());
        
        map = new Node[N][N];
        sum = new int[N][N];
        sumTmp = new int[N][N];
        
        for (int i = 1; i < N; i++) {
        	st = new StringTokenizer(br.readLine());
			for (int j = 1; j < N; j++) {
//				System.out.println(st.countTokens());
				byte type = (byte) Integer.parseInt(st.nextToken());
				map[i][j] = new Node(type, false, false);
				if(type == 1) desks.add(new Desk((byte)i, (byte)j));
				else if(type > 1) aircons.add(new Aircon(i, j, type-2, 0));
			}
		}
        
        for (int i = 1; i < M; i++) {
        	st = new StringTokenizer(br.readLine());
        	int y = Integer.parseInt(st.nextToken());
        	int x = Integer.parseInt(st.nextToken());
        	int s = Integer.parseInt(st.nextToken());
        	
        	if(s == 0) map[y][x].up = true;
        	else map[y][x].left = true;
		}
        
        int answer = 0;
        while(true) {
        	if(check()) break;
        	wind();
        	mix();
        	minus();
        	answer++;
//        	System.out.println("sum " + answer);
//        	for (int i = 0; i < N; i++) {
//    			for (int j = 0; j < N; j++) {
//    				System.out.print(sum[i][j] + " ");
//    			}
//    			System.out.println();
//    		}
        	if(answer >= 100) {
        		answer = -1;
        		break;
        	}
        }
        System.out.println(answer);
    }
    public static boolean check() {
    	for (Desk desk : desks) {
			int y = desk.y;
			int x = desk.x;
			if(sum[y][x] < K) return false;
		}
    	return true;
    }
    public static void wind() {
    	
    	for (Aircon aircon : aircons) {
    		boolean[][] visit = new boolean[N][N];
    		
    		int ny = aircon.y + dy[aircon.dir];
    		int nx = aircon.x + dx[aircon.dir];
    		if(ny <= 0 || nx <= 0 || ny >= N || nx >= N) continue;
    		sumTmp[ny][nx] += 5;
    		visit[ny][nx] = true;
    		queue.offer(new Aircon(ny, nx, aircon.dir, 5));
			bfs(visit);
		}
    	
    	for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				sum[i][j] += sumTmp[i][j];
				sumTmp[i][j] = 0;
			}
		}
    	
//    	System.out.println("sum");
//    	for (int i = 0; i < N; i++) {
//			for (int j = 0; j < N; j++) {
//				System.out.print(sum[i][j] + " ");
//			}
//			System.out.println();
//		}
    }
    public static void bfs(boolean[][] visit) {
    	while(!queue.isEmpty()) {
    		Aircon aircon = queue.poll();
    		if(aircon.power == 1) continue; 
    		
    		for (int d = 0; d < 8; d++) { // 상 우상 우 우하 하 좌하 좌 좌상)
				if (aircon.dir == 0 && !(d == 5 || d == 6 || d == 7)) continue; //왼쪽
				else if (aircon.dir == 1 && !(d == 0 || d == 1 || d == 7)) continue; //위
				else if (aircon.dir == 2 && !(d == 1 || d == 2 || d == 3)) continue; //오른쪽
				else if (aircon.dir == 3 && !(d == 3 || d == 4 || d == 5)) continue; //아래
				
				int ny = aircon.y + wy[d];
				int nx = aircon.x + wx[d];
				if (ny <= 0 || nx <= 0 || ny >= N || nx >= N || visit[ny][nx]) continue;
				
				if (aircon.dir == 0 && d == 7 && (map[aircon.y][aircon.x].up || map[ny][aircon.x].left)) continue;
				else if (aircon.dir == 0 && d == 6 && (map[ny][aircon.x].left)) continue;
				else if (aircon.dir == 0 && d == 5 && (map[ny][aircon.x].up || map[ny][aircon.x].left)) continue;
				else if (aircon.dir == 1 && d == 7 && (map[aircon.y][aircon.x].left || map[aircon.y][nx].up)) continue;
				else if (aircon.dir == 1 && d == 0 && (map[aircon.y][aircon.x].up )) continue;
				else if (aircon.dir == 1 && d == 1 && (map[aircon.y][nx].left || map[aircon.y][nx].up)) continue;
				else if (aircon.dir == 2 && d == 1 && (map[aircon.y][aircon.x].up || map[ny][nx].left)) continue;
				else if (aircon.dir == 2 && d == 2 && (map[aircon.y][nx].left )) continue;
				else if (aircon.dir == 2 && d == 3 && (map[ny][aircon.x].up || map[ny][nx].left)) continue;
				else if (aircon.dir == 3 && d == 5 && (map[aircon.y][aircon.x].left || map[ny][nx].up)) continue;
				else if (aircon.dir == 3 && d == 4 && (map[ny][aircon.x].up )) continue;
				else if (aircon.dir == 3 && d == 3 && (map[aircon.y][nx].left || map[ny][nx].up)) continue;
				
				visit[ny][nx] = true;
				sumTmp[ny][nx] += (aircon.power-1);
				queue.offer(new Aircon(ny, nx, aircon.dir, aircon.power-1));
			}
    		
    		
    	}
    }
    
    public static void mix() {
    	int[][] mixTmp = new int[N][N];
    	
    	for (int i = 1; i < N; i++) {
			for (int j = 1; j < N; j++) {
				for (int d = 0; d < 4; d++) {// 좌상우하
					int ny = i + dy[d];
					int nx = j + dx[d];
					if(ny <= 0 || nx <= 0 || ny >= N || nx >= N) continue;
					if(d==0 && map[i][j].left) continue;
					else if(d==1 && map[i][j].up ) continue;
					else if(d==2 && map[ny][nx].left) continue;
					else if(d==3 && map[ny][nx].up ) continue;
					
					int diff = Math.abs(sum[i][j] - sum[ny][nx]) / 4;
					if(sum[i][j] > sum[ny][nx]) mixTmp[i][j] -= diff;
					else mixTmp[i][j] += diff;
				}
			}
		}
    	
    	for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				sum[i][j] += mixTmp[i][j];
			}
		}
//    	System.out.println("mix");
//    	for (int i = 0; i < N; i++) {
//			for (int j = 0; j < N; j++) {
//				System.out.print(sum[i][j] + " ");
//			}
//			System.out.println();
//		}
    }
    
    public static void minus() {
    	
    	for (int j = 1; j < N; j++) {
			if(sum[1][j] == 0) continue;
			sum[1][j] -= 1;
		}
    	
    	for (int j = 1; j < N; j++) {
			if(sum[N-1][j] == 0) continue;
			sum[N-1][j] -= 1;
		}
    	
    	for (int i = 2; i < N-1; i++) {
			if(sum[i][1] == 0) continue;
			sum[i][1] -= 1;
		}
    	
    	for (int i = 2; i < N-1; i++) {
			if(sum[i][N-1] == 0) continue;
			sum[i][N-1] -= 1;
		}

//    	System.out.println("minus");
//    	for (int i = 0; i < N; i++) {
//			for (int j = 0; j < N; j++) {
//				System.out.print(sum[i][j] + " ");
//			}
//			System.out.println();
//		}
    }

    
    public static class Node{
    	byte type;
    	boolean up;
    	boolean left;
    	public Node(byte type, boolean up, boolean left) {
    		this.type = type;
    		this.up = up;
    		this.left = left;
    	}
    }
    
    public static class Aircon{
    	byte y;
    	byte x;
    	byte dir;
    	byte power;

    	public Aircon(int y, int x, int dir, int power) {
    		this.y = (byte) y;
    		this.x = (byte) x;
    		this.dir = (byte) dir;
    		this.power = (byte) power;
    	}
    }
    
    public static class Desk{
    	byte y;
    	byte x;

    	public Desk(byte y, byte x) {
    		this.y = y;
    		this.x = x;
    	}
    }
 
}