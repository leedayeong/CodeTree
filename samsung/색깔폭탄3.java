package samsung;

import java.io.*;
import java.util.*;

/*
4 3
1 2 2 -1
1 1 -1 -1
0 -1 2 2
3 3 3 2


38
 */
public class 색깔폭탄3{
	
	static int N, M;
	
	static final int EMPTY = -2;
	
	static int [][] map;
	static int [][] mapTmp;
	
	static boolean[][] visit;
	static boolean[][] realVisit;
	static Queue<Bomb> queue;
	static boolean check;
	static int numsTmp;
	static int numsRedTmp;
	static int manyY;
	static int manyX;
	static int sum;
	static int[] dy = {-1, 1, 0, 0};
	static int[] dx = {0, 0, -1, 1};
	
    public static void main(String[] args) throws Exception {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	StringTokenizer st = new StringTokenizer(br.readLine());
    	N = Integer.parseInt(st.nextToken());
    	M = Integer.parseInt(st.nextToken());
    	
    	sum = 0;
    	map = new int[N][N];
    	mapTmp = new int[N][N];
    	visit = new boolean[N][N];
    	realVisit = new boolean[N][N];
    	
    	queue = new LinkedList<>();

    	
    	for (int i = 0; i < N; i++) {
    		st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
    	int cnt = 1;
    	while(true) {
//    		System.out.println(cnt++);
			if(!find()) break;
			
			remove();
	    	
			drop();
	    
			rotate();
	    	
			drop();
//			System.out.println(sum);
//			if(cnt==6)break;
    	}
    	System.out.println(sum);

    }
    public static boolean find() {
    	
    	for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				realVisit[i][j] = false;
			}
		}
    	
    	
    	Bundle best = new Bundle(-1, -1, -1, -1);
    	
    	for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if(map[i][j] <= 0)continue;
				Bundle comp = bfs(i, j);
				
				if (comp.isHigher(best)) {
					best = comp;
					
					for (int k = 0; k < N; k++) {
						for (int f = 0; f < N; f++) {
							realVisit[k][f] = visit[k][f];
						}
					}
				}
			}
		}
    	
//    	System.out.println("total : " + best.total);
    	if (best.total == -1 || best.total == 1) return false;
    	else {
    		sum += (best.total * best.total);
    		return true;
    	}
    }
    
    public static void remove() {
    	for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if(realVisit[i][j]) map[i][j] = EMPTY;
			}
		}
    }
   
    public static Bundle bfs(int y, int x) {
    	numsTmp = 0;
    	numsRedTmp = 0;
    	
    	for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				visit[i][j] = false;
			}
		}
    	
    	visit[y][x] = true;
    	int color = map[y][x];
    	queue.offer(new Bomb(y, x));
    	
    	while(!queue.isEmpty()) {
    		
    		Bomb bomb = queue.poll();
    		
    		for (int d = 0; d < 4; d++) {
				int ny = bomb.y + dy[d];
				int nx = bomb.x + dx[d];
				
				if(ny < 0 || nx < 0 || ny >= N || nx >= N || visit[ny][nx]) continue;
				if(map[ny][nx] == color || map[ny][nx] == 0) {
					queue.offer(new Bomb(ny, nx));
					visit[ny][nx] = true;
				}
			}
    	}
    	
    	for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if(!visit[i][j]) continue;
				numsTmp ++;
				if(map[i][j] == 0) numsRedTmp++;
			}
		}
    	
    	
    	return new Bundle(y, x, numsTmp, numsRedTmp);
    	
    }
    
    public static void drop() {
    	
    	for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				mapTmp[i][j] = EMPTY;
			}
		}
    	
    	
    	for (int j = 0; j < N; j++) {
    		int lastIdx = N-1;
    		for (int i = N-1; i >= 0; i--) {
				if(map[i][j] == EMPTY) continue;
				if(map[i][j] == -1) lastIdx = i;
				
				mapTmp[lastIdx--][j] = map[i][j];
			}
		}
    	
    	for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				map[i][j] = mapTmp[i][j];
			}
		}
    	
    }
    
    public static void rotate() {
    	for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				mapTmp[i][j] = EMPTY;
			}
		}
    	
    	
    	for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				mapTmp[i][j] = map[j][N-1-i];
			}
		}
    	
    	for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				map[i][j] = mapTmp[i][j];
			}
		}
    	
    }
    
    
    
    public static class Bomb{
    	byte y;
    	byte x;
    	
    	public Bomb(int y, int x) {
    		this.y = (byte) y;
    		this.x = (byte) x;
    	}
    }
    
    public static class Bundle{
    	int total;
    	int red;
    	int y;
    	int x;
    	
    	public Bundle(int y, int x, int total, int red) {
    		this.y =  y;
    		this.x =  x;
    		this.total = total;
    		this.red = red;
    	}
    	
    	public boolean isHigher(Bundle b) {
    		if(this.total != b.total) return this.total > b.total;
    		else if(this.red != b.red) return this.red < b.red;
    		else if(this.y != b.y)  return this.y > b.y;
    		else return this.x < b.x;
    			
    	}
    }


}