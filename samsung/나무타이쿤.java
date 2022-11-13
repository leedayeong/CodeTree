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
public class 나무타이쿤{
	
	static int N, M;
	static Rule[] rules;
	
	static int[][] map;
	
	static Queue<Supplement> supplements = new LinkedList<>();
	
	static int[] dy = {0, 0, -1, -1, -1, 0, 1, 1, 1 }; // 우, 우상, 상, 좌상, 좌, 좌하, 하, 우하
	static int[] dx = {0, 1, 1, 0, -1, -1, -1, 0, 1};
	
	static int[] wy = {-1, -1, 1, 1}; // 좌상, 우상, 우하, 좌하
	static int[] wx = {-1, 1, 1, -1};
	
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        
        map = new int[N][N];
        
        for (int i = 0; i < N; i++) {
        	st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
        
        rules = new Rule[M];
        
        for (int i = 0; i < M; i++) {
        	st = new StringTokenizer(br.readLine());
			rules[i] = new Rule(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
		}
        
        
        supplements.offer(new Supplement(N-2, 0));
        supplements.offer(new Supplement(N-2, 1));
        supplements.offer(new Supplement(N-1, 0));
        supplements.offer(new Supplement(N-1, 1));
        
        for (int k = 0; k < M; k++) {
			move(k);
			plus();
		}
        
        int answer = 0;
        
    	for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				answer += map[i][j];
			}
//			System.out.println();
		}
    	System.out.println(answer);
        
    }
    public static void move(int k) {
    	
    	int len = supplements.size();
    	
    	for (int i = 0; i < len; i++) {
			Supplement s = supplements.poll();
			
			int ny = ( s.y + (dy[rules[k].d] * rules[k].p) ) % N;
			int nx = ( s.x + (dx[rules[k].d] * rules[k].p) ) % N  ;
			
			ny = ny < 0 ? ny + N : ny;
			nx = nx < 0 ? nx + N : nx;
			
			supplements.offer(new Supplement(ny, nx));
			map[ny][nx] ++; //높이 1증가
    	}
    	
    	
//    	for (int i = 0; i < N; i++) {
//			for (int j = 0; j < N; j++) {
//				System.out.print(map[i][j] + " ");
//			}
//			System.out.println();
//		}
    }
    
    public static void plus() {
    	boolean [][] visit = new boolean[N][N];
    	int [][] plusTmp = new int[N][N];
    	
    	int len = supplements.size();
    	
    	for (int i = 0; i < len; i++) {
			Supplement s = supplements.poll();
			
			int cnt = 0;
			
			for (int d = 0; d < 4; d++) {
				int ny = s.y + wy[d];
				int nx = s.x + wx[d];
				
				if(ny < 0 || nx < 0 || ny >= N || nx >= N) continue;
				if(map[ny][nx] >= 1) cnt++;
			}
			
			visit[s.y][s.x] = true;
			plusTmp[s.y][s.x] = cnt; 
//			supplements.offer(new Supplement(s.y, s.x));
    	}
    	
    	for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if(!visit[i][j] && map[i][j] >= 2) {
					map[i][j] -= 2;
					supplements.offer(new Supplement(i, j));
				}
				map[i][j] += plusTmp[i][j];
			}
		}
    	
//    	for (int i = 0; i < N; i++) {
//			for (int j = 0; j < N; j++) {
//				System.out.print(map[i][j] + " ");
//			}
//			System.out.println();
//		}	
    }

    
	
	public static class Rule{
		byte d;
		byte p;
		public Rule(int d, int p) {
			this.d = (byte) d;
			this.p = (byte) p;
		}
	}
	
	public static class Supplement{
		byte y;
		byte x;
		
		public Supplement(int y, int x) {
			this.y = (byte) y;
			this.x = (byte) x;
		}
	}

}