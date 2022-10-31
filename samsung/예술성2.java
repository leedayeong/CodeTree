package samsung;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/*


5
1 2 2 3 3
2 2 2 3 3
2 2 1 3 1
2 2 1 1 1
2 2 1 1 1

1771
 */

class 예술성{
	
	public static int N;
	public static int name;
	public static int cnt;
	public static int result;
	
	public static int[][] map;
	public static int[][] newMap;
	public static Node[][] group;
	
	public static Map<Integer, Node> groups;
	
	public static boolean[][] visit;
	public static int[] src;
	public static int[] tgt;
	
	public static int[] dy = {-1, 1, 0, 0};
	public static int[] dx = {0, 0, -1, 1};
	
	public static boolean[] select;
	
	public static void main(String args[]) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		N = Integer.parseInt(br.readLine());
		
		map = new int[N][N];
		visit = new boolean[N][N];
		result = 0;
		StringTokenizer st;
		
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		for (int t = 0; t < 4; t++) {
			
			grouping();
			calc();
			rotate();
//			for (int i = 0; i < N; i++) {
//				for (int j = 0; j < N; j++) {
//					System.out.print(map[i][j] + " ");
//				}
//				System.out.println();
//			}
			
		}
		System.out.println(result);
	}
	public static void grouping() {
		visit = new boolean[N][N];
		group = new Node[N][N];
		groups = new HashMap<>();
		newMap = new int[N][N];
		
//		result = 0; // test
		
		name = 1;
		cnt = 1;
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if(!visit[i][j]) {
//					System.out.println("new group! ");
//					System.out.println( "[" + i + ", " + j + "]" + "color : " + map[i][j] +  ", name : " + name);
					visit[i][j] = true;
					group[i][j] = new Node (map[i][j], name, 0);
					cnt = 1;
					dfs(i, j, map[i][j], name);
					groups.put(name, new Node(map[i][j], name++, cnt));
				}
			}
		}
	}
	
	public static void dfs(int y, int x, int color, int num) {
		
		for (int d = 0; d < 4; d++) {
			int ny = y + dy[d];
			int nx = x + dx[d];
			
			if(ny < 0 || nx < 0 || ny >= N || nx >= N || visit[ny][nx]) continue;
			if(color != map[ny][nx]) continue;
			group[ny][nx] = new Node (map[ny][nx], num, 0);
			visit[ny][nx] = true;
			cnt++;
			dfs(ny, nx, map[ny][nx], num);
		}
	}
	
	public static void calc() {
		select = new boolean[name-1];
		src = new int[name-1];
		tgt = new int[2];
		
		comb(0, 0);
	}
	
	public static void comb(int srcIdx, int tgtIdx) {
		if(tgtIdx == 2) {
			//complete
			Node a = null;
			Node b = null;
			for (int i = 0; i < name-1; i++) {
				if(select[i] && a == null) {
					a = groups.get(i+1);
					continue;
				}
				else if(select[i] && b == null) {
					b = groups.get(i+1);
					break;
				}
			}

			int tmp = ((a.cnt + b.cnt) * a.color * b.color * cntCheck(a, b));
			result += ((a.cnt + b.cnt) * a.color * b.color * cntCheck(a, b));
//			System.out.println("a cnt : " + a.cnt + ", b cnt : " + b.cnt);
//			System.out.println("a 와 b조합 , a : " + a.name + "(" + a.color + ")" + " , b : " +  b.name  + "(" + b.color + ")" + " => sum : " + tmp);
			return;
		}
		if(srcIdx == name-1) return;
		
		select[srcIdx] = true;
		comb(srcIdx+1, tgtIdx+1);
		
		select[srcIdx] = false;
		comb(srcIdx+1, tgtIdx);
		
		
	}
	
	public static int cntCheck(Node a, Node b) {
		int count = 0;
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if(group[i][j].name == a.name) {
					for (int d = 0; d < 4; d++) {
						int ny = i + dy[d];
						int nx = j + dx[d];
						
						if(ny < 0 || nx < 0 || ny >= N || nx >= N) continue;
						if(group[ny][nx].name == b.name) count++;
					}
				}
			}
		}
		return count;
	}
	public static void rotate() {
		newMap = new int[N][N];
		
		int[] row = new int[N];
		int[] col = new int[N];
		
		for (int i = 0; i < N; i++) {
			row[i] = map[N/2][i];
		}
		for (int i = 0; i < N; i++) {
			col[i] = map[i][N/2];
		}
		
		for (int i = 0; i < N; i++) {
			newMap[N/2][i] = col[i]; 
		}
		for (int i = 0; i < N; i++) {
			newMap[i][N/2] = row[N-i-1];
		}
		
		rotateSquare(0, 0, N/2);
		rotateSquare(0, N/2+1, N/2);
		rotateSquare(N/2+1, 0, N/2);
		rotateSquare(N/2+1, N/2+1, N/2);
		
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				map[i][j] = newMap[i][j];
			}
		}
		
	}
	
	public static void rotateSquare(int sy, int sx, int squareN) {
		
		for (int i = sy; i < sy + squareN; i++) {
			for (int j = sx; j < sx + squareN; j++) {
				int oy = i-sy;
				int ox = j-sx;
				
				int ny = ox;
				int nx = squareN - oy -1;
				
				newMap[ny + sy][nx + sx] = map[i][j];
				
			}
		}
	}
	
	
	public static class Node{
		int color;
		int name;
		int cnt;
		
		public Node(int color, int name, int cnt) {
			this.color = color;
			this.name = name;
			this.cnt = cnt;
		}
	}
}