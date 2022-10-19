package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/*
5 1 2 1
0 0 0 0 0
0 30 23 0 0
0 0 -1 0 0
0 0 17 46 77
0 0 0 12 0


 */
public class 나무박멸 {
	
	static int N, M, K, C;

	static final int WALL = -1;
	static final int EMPTY = 0;
	
	static Product best;
	
	static Node[][] map;
	static int[][] temp;
	
	static long sum;
	
	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken()); // 격자 크기
		M = Integer.parseInt(st.nextToken()); // 박멸 진행되는 년 수
		K = Integer.parseInt(st.nextToken()); // 제초제 확산 범위
		C = Integer.parseInt(st.nextToken()); // 제초제 남아있는 년 수
		
		map = new Node[N][N];
		temp = new int[N][N];
		sum = 0;
		
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				map[i][j] = new Node(Integer.parseInt(st.nextToken()), 0);
			}
		}
		

		
		for (int i = 0; i < M; i++) {
			grow();

			spread();

			findAttack();

			attack();

		}
		
		System.out.println(sum);
		
	}

	public static void grow() {
		
		int dy[] = {-1, 1, 0, 0}; //상하좌우
		int dx[] = {0, 0, -1, 1};
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if(map[i][j].num > 0) {
					int cnt = 0;
					for (int d = 0; d < 4; d++) {
						int ny = i + dy[d];
						int nx = j + dx[d];
						if(ny < 0 || nx < 0 || ny >= N || nx >= N) continue;
						if(map[ny][nx].num > 0) cnt++;
					}
					map[i][j].num += cnt;
				}
			}
		}
		
		
//		System.out.println("grow");
//		for (int i = 0; i < N; i++) {
//			for (int j = 0; j < N; j++) {
//				System.out.printf("%3d", map[i][j].num);
//			}
//			System.out.println();
//		}
	}
	public static void spread() {
		
		int dy[] = {-1, 1, 0, 0}; //상하좌우
		int dx[] = {0, 0, -1, 1};
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				temp[i][j] = 0;
			}
		}
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if(map[i][j].num > 0) {
					int cnt = 0;
					for (int d = 0; d < 4; d++) {
						int ny = i + dy[d];
						int nx = j + dx[d];
						if(ny < 0 || nx < 0 || ny >= N || nx >= N) continue;
						if(map[ny][nx].c > 0) continue;
						if(map[ny][nx].num == EMPTY) cnt++;
					}
					
					if(cnt==0) continue;
					for (int d = 0; d < 4; d++) {
						int ny = i + dy[d];
						int nx = j + dx[d];
						if(ny < 0 || nx < 0 || ny >= N || nx >= N) continue;
						if(map[ny][nx].c > 0) continue;
						if(map[ny][nx].num == EMPTY)  temp[ny][nx] += (map[i][j].num / cnt);
					}
				}
			}
		}
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				map[i][j].num += temp[i][j];
			}
		}
		
//		System.out.println("spread");
//		for (int i = 0; i < N; i++) {
//			for (int j = 0; j < N; j++) {
//				System.out.printf("%8d", map[i][j].num);
//			}
//			System.out.println();
//		}
	}
	
	public static void findAttack() {
		// 제초제 일년 지남
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if(map[i][j].c > 0) {
					map[i][j].c -= 1;
				}
			}
		}
		
		best = new Product(0, 0, 0);
		
		int dy[] = {-1, -1, 1, 1}; //좌상 우상 우하 좌하
		int dx[] = {-1, 1, 1, -1};

		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if(map[i][j].num > 0) {
//					System.out.println("me: " + map[i][j].num);
					int sumTmp = map[i][j].num;

					for (int d = 0; d < 4; d++) {
						
						for (int k = 1; k <= K; k++) {
							int ny = i + dy[d] * k;
							int nx = j + dx[d] * k;
							
							if(ny < 0 || nx < 0 || ny >= N || nx >= N) break;
//							if(map[ny][nx].c > 0) continue;
							if(map[ny][nx].num == WALL || map[ny][nx].num == EMPTY ) break;
							
							sumTmp += map[ny][nx].num;
						}
					}
					
					Product p = new Product(sumTmp, i, j);
					
					if(p.isHigher(best)) best = p;

				}
			}
		}
		
//		System.out.println();
//		System.out.println("cnt : " + best.cnt + " y :" + best.y + ", x :" + best.x + ", me :" + map[best.y][best.x].num);
		sum += best.cnt;
	}
	public static void attack() {
		
		int dy[] = {-1, -1, 1, 1}; //좌상 우상 우하 좌하
		int dx[] = {-1, 1, 1, -1};
		
		if(map[best.y][best.x].num > 0) {
			// 제초제 뿌리기
			map[best.y][best.x].num = 0;
			map[best.y][best.x].c = C;
			for (int d = 0; d < 4; d++) {
				
				for (int k = 1; k <= K; k++) {
					int ny = best.y + dy[d] * k;
					int nx = best.x + dx[d] * k;
					
					if(ny < 0 || nx < 0 || ny >= N || nx >= N) break;
					
					if(map[ny][nx].num == WALL) break;
					if(map[ny][nx].num == EMPTY) {
						map[ny][nx].num = 0;
						map[ny][nx].c = C;
						break;
					}
					map[ny][nx].num = 0;
					map[ny][nx].c = C;
				}
			}
		}

//		System.out.println("attack");
//		for (int i = 0; i < N; i++) {
//			for (int j = 0; j < N; j++) {
//				System.out.printf("%8d", map[i][j].num);
//			}
//			System.out.println();
//		}
	}
	
	public static class Node{
		int num;
		int c;
		public Node(int num, int c) {
			this.num = num;
			this.c = c;
		}
	}
	
	public static class Product{
		int cnt;
		int y;
		int x;
		public Product(int cnt, int y, int x) {
			this.cnt = cnt;
			this.y = y;
			this.x = x;
		}
		public boolean isHigher(Product p) {
			if(this.cnt != p.cnt) return this.cnt > p.cnt;
			else if(this.y != p.y) return this.y < p.y;
			else return this.x <= p.x;
		}
	}

}
