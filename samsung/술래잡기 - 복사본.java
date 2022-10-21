package samsung;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

/*
n m h k

5 3 1 1
2 4 1
1 4 2
4 2 1
2 4

1
 */

class Solution{
	
	static int N;
	static int M;
	static int K;
	
	static int sulY;
	static int sulX;
	static boolean reverse = false;
	static int dir = 0;
	static int dirNum = 1;
	static int dirCnt = 0;
	static int cnt = 0;
	
	static int[] dy = {-1, 0, 1, 0};
	static int[] dx = {0, 1, 0, -1};
	static int[] ry = {1, 0, -1, 0}; // reverse
	static int[] rx = {0, 1, 0, -1};
	
	static int num = 0;
	
	
	static boolean[][] tree;
	static boolean[][] people;
	static List<Person> list;
	
	
	public static void main(String args[]) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int score = 0;
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		int h = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		
		tree = new boolean[N+1][N+1];
		people = new boolean[N+1][N+1];
		list = new LinkedList<>();
		
		sulY = N/2 + 1;
		sulX = N/2 + 1;
		
		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine()); 
			int y = Integer.parseInt(st.nextToken());
			int x = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken()); //1 : 좌우, 2: 상하
			list.add(new Person(y, x, d, 1));
			people[y][x] = true;
		}
		
		for (int i = 0; i < h; i++) {
			st = new StringTokenizer(br.readLine()); 
			int y = Integer.parseInt(st.nextToken());
			int x = Integer.parseInt(st.nextToken());	
			tree[y][x] = true;
		}

		for (int i = 1; i <= K ; i++) {
			num = 0;
			reset();
			move();
			sulMove();
			score += (i * num);
			
		}
		
		System.out.println(score);
		
		
	}
	public static void move() {
		int len = list.size();
		for (int i = 0; i < len; i++) {
			Person node = list.remove(0);
			if(Math.abs(node.y - sulY) + Math.abs(node.x - sulX) > 3) {
				list.add(node);
				continue;
			}
			if(node.d == 1) { // 좌우
				int ny = node.y;
				int nx = node.x + node.cur;
				
				if(nx <= 0 || nx > N) {
					//범위 넘어감
					node.cur = node.cur == 1? -1 : 1;
					nx = node.x + node.cur;
				} 
				if(ny == sulY && nx == sulX) {
					people[node.y][node.x] = true;
					list.add(new Person(node.y, node.x, node.d, node.cur));
				}
				else {
					people[ny][nx] = true;
					list.add(new Person(ny, nx, node.d, node.cur));
				}
				
			}else { // 상하
				int ny = node.y + node.cur;
				int nx = node.x;
				
				if(ny <= 0 || ny > N) {
					//범위 넘어감
					node.cur = node.cur == 1? -1 : 1;
					ny = node.y + node.cur;
				} 
				if(ny == sulY && nx == sulX) {
					people[node.y][node.x] = true;
					list.add(new Person(node.y, node.x, node.d, node.cur));
				}
				else {
					people[ny][nx] = true;
					list.add(new Person(ny, nx, node.d, node.cur));
				}
			}
		}
	}
	
	public static void reset() {
		for (int i = 1; i <= N; i++) {
			for (int j = 0; j <= N; j++) {
				people[i][j] = false;
			}
		}
	}
	
	public static void watch() {
		int y = sulY;
		int x = sulX;
		if(!tree[y][x] && people[y][x])  catchPerson(y, x);
		
		if(!reverse) {
			y += dy[dir];
			x += dx[dir];
		}else {
			y += ry[dir];
			x += rx[dir];
		}
			
		if(y <= 0 || y > N || x <= 0 || x > N) return;
		
		if(!tree[y][x] && people[y][x])  catchPerson(y, x);
			
		if(!reverse) {
			y += dy[dir];
			x += dx[dir];
		}else {
			y += ry[dir];
			x += rx[dir];
		}
		
		if(y <= 0 || y > N || x <= 0 || x > N) return;
		
		if(!tree[y][x] && people[y][x])  catchPerson(y, x);
	}
	
	public static void catchPerson(int y, int x) {
		int len = list.size();
		for (int i = 0; i < len; i++) {
			Person node = list.remove(0);
			if(node.y == y && node.x == x) num++;
			else list.add(node);
			
		}
	}
	
	public static void sulMove() {
		if(!reverse) {// false : 시계 방향
			sulY += dy[dir];
			sulX += dx[dir];
			cnt++;
			if(cnt == dirNum) {
				dir = (dir+1)%4;
				cnt = 0;
				dirCnt++;
				
				if(dirNum + 1 != N && dirCnt == 2) {
					dirNum ++;
					dirCnt = 0;
				}
				else if(dirNum + 1 == N && dirCnt == 3) {
					reverse = true;
					dirCnt = 0;
					dir = 0;
				}
			}
			
		}else {
			sulY += ry[dir];
			sulX += rx[dir];
			cnt++;
			if(cnt == dirNum) {
				dir = (dir+1)%4;
				cnt = 0;
				dirCnt++;
				
				if(dirNum + 1 == N && dirCnt == 3 || dirNum + 1 != N && dirNum != 1 && dirCnt == 2) {
					dirNum --;
					dirCnt = 0;
				}
				else if(dirNum == 1 && dirCnt == 2) {
					reverse = false;
					dirCnt = 0;
					dir = 0;
				}
			}
		}
		watch();
	}
	
	public static class Person{
		int y = 0;
		int x = 0;
		int d = 0;//1 : 좌우, 2: 상하
		int cur = 1; // 우 또는 하
		
		public Person(int y, int x, int d, int cur) {
			this.y = y;
			this.x = x;
			this.d = d;
			this.cur =cur;
		}
	}
}