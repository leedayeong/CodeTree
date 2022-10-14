package samsung;

import java.io.*;
import java.util.*;

/*
3
3 5 8 9 2
6 1 2 3 4
1 5 8 7 6
8 2 5 3 1
9 4 8 2 1
4 6 5 7 8
7 3 4 2 9
2 1 6 3 5
5 4 3 8 6


170
 */
public class 놀이기구탑승{
	
	static int N;
	static int M;
	static int sum;
	
	static int[][] seats;
	static int[][] likeList;
	
	static HashMap<Integer, Student> students;
	
	static int[] dy = {-1, 1, 0, 0}; // 상 하 좌 우 
	static int[] dx = {0, 0, -1, 1};
	
	static int[] score = {0, 1, 10, 100, 1000};
	
    public static void main(String[] args) throws IOException {
       BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
       N = Integer.parseInt(br.readLine());
       M = N*N;
       
       seats = new int[N][N];
       likeList = new int[M][5];
       
       for (int i = 0; i < M; i++) {
    	   StringTokenizer st = new StringTokenizer(br.readLine());
    	   for (int j = 0; j < 5; j++) {
			likeList[i][j] = Integer.parseInt(st.nextToken());
    	   }
       }
       
       students = new HashMap<>();
       
       seats[1][1] = likeList[0][0];
       students.put(likeList[0][0], new Student(1, 1));
       
       for(int i = 1; i < M; i++) {
    	   sit(i);
       }
       
       sum = 0;
       calc();
       System.out.println(sum);
       
    }
    public static void sit(int k) {
    	int[][] tmp = new int[N][N];
    	
    	for (int j = 1; j < 5; j++) {
			if(students.containsKey(likeList[k][j])) {
				Student student = students.get(likeList[k][j]);
				for (int d = 0; d < 4; d++) {
					int ny = student.y + dy[d];
					int nx = student.x + dx[d];
					
					if(ny < 0 || nx < 0 || ny >= N || nx >= N) continue;
					if(seats[ny][nx]==0)tmp[ny][nx]++;
				}
			}
		}
    	
    	int y = 0;
    	int x = 0;
    	int manyFriends = -1;
    	int empty = 0;
    	for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if(tmp[i][j] > manyFriends && seats[i][j] == 0) {
					manyFriends = tmp[i][j];
					y = i;
					x = j;
					empty = 0;
					for (int d = 0; d < 4; d++) {
						int ny = y + dy[d];
						int nx = x + dx[d];
						
						if(ny < 0 || nx < 0 || ny >= N || nx >= N) continue;
						if(seats[ny][nx] == 0) empty++;
					}
				}else if(tmp[i][j] == manyFriends && seats[i][j] == 0) {
					int compareEmpty = 0;
					for (int d = 0; d < 4; d++) {
						int ny = i + dy[d];
						int nx = j + dx[d];
						
						if(ny < 0 || nx < 0 || ny >= N || nx >= N) continue;
						if(seats[ny][nx] == 0) compareEmpty++;
					}
					if(compareEmpty > empty) {
						manyFriends = tmp[i][j];
						y = i;
						x = j;
						empty = compareEmpty;
					}
				}
			}
		}
    	
        seats[y][x] = likeList[k][0];
        students.put(likeList[k][0], new Student(y, x));
    	
        
//        System.out.println("seat "+ k + "번째");
//        for (int i = 0; i < N; i++) {
//			for (int j = 0; j < N; j++) {
//				System.out.print(seats[i][j] + " ");
//			}
//			System.out.println();
//		}
        
    }
    
    public static void calc() {
    	
    	for (int i = 0; i < M; i++) {
    		Student mySeat = students.get(likeList[i][0]);
    		int cnt = 0;
			for (int j = 1; j < 5; j++) {
				Student fSeat = students.get(likeList[i][j]);
				
				int diff = Math.abs(mySeat.y - fSeat.y) + Math.abs(mySeat.x - fSeat.x);
				
				if(diff == 1) cnt++;
			}
//			System.out.println(likeList[i][0] + " => cnt : " + cnt);
			sum += score[cnt];
		}
    }
    
    
    public static class Student{
    	int y;
    	int x;
    	public Student(int y, int x) {
    		this.y = y;
    		this.x = x;
    	}
    }


}