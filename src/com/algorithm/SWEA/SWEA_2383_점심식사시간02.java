package com.algorithm.SWEA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class SWEA_2383_점심식사시간02 {
	static int N, min, cnt; // 맵의 크기, 최소시간, 사람의수
	static boolean[] selected; // 부분집합 선택을 관리할 배열

	static int sList[][]; // 계단정보
	static final int M = 1, W = 2, D = 3, C = 4; // 사람들의 상태 : 이동중, 대기, 계단 내려가는중, 완료
	static ArrayList<Person> pList; // 사람들 리스트

	static class Person implements Comparable<Person> {
		int r, c, downCnt, status, time; // 행좌표, 열좌표, 내려간계단수, 입구도착시간

		public Person(int r, int c) {
			super();
			this.r = r;
			this.c = c;
		}

		public void init() {
			downCnt = 0;
			status = M;
			time = 0;
		}

		@Override
		public int compareTo(Person o) {
			// TODO Auto-generated method stub
			return this.time - o.time;
		}
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		for (int t = 1; t <= T; t++) {
			N = sc.nextInt();
			pList = new ArrayList<Person>();
			sList = new int[2][];
			min = Integer.MAX_VALUE;
			int c = 0, k = 0;
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					c = sc.nextInt();
					if (c == 1)
						pList.add(new Person(i, j));
					else if (c > 1)
						sList[k++] = new int[] { i, j, c }; // 좌표, 계단높이
				}
			}
			cnt = pList.size();
			selected = new boolean[cnt];
			divide(0);
			System.out.println("#" + t + " " + min);
		}

	}

	private static void divide(int index) { // 부분집합으로 계단 배정하기

		if (index == cnt) {
			makeList();
			return;
		}
		selected[index] = true;
		divide(index + 1);
		selected[index] = false;
		divide(index + 1);

	}

	private static void makeList() { // 계단의 배정된 상황에 따라 각각의 사람리스트 생성
		ArrayList<Person> aList = new ArrayList<Person>();
		ArrayList<Person> bList = new ArrayList<Person>();
		// t t t t f f

		for (int i = 0; i < cnt; i++) {
			Person p = pList.get(i);
			p.init();
			if (selected[i]) {
				p.time = Math.abs(p.r - sList[0][0]) + Math.abs(p.c - sList[0][1]);
				aList.add(p);
			} else {
				p.time = Math.abs(p.r - sList[1][0]) + Math.abs(p.c - sList[1][1]);
				bList.add(p);

			}

		}

		int res = go(aList, bList);
		if (min > res)
			min = res;
	}

	// 두 사람리스트를 내려가기 처리후 소요시간 비교해서 모든 사람이 내려가는데 걸리는 시간을 결정
	private static int go(ArrayList<Person> aList, ArrayList<Person> bList) {
		int timeA = 0, timeB = 0;
		if (aList.size() > 0)
			timeA = processDown(aList, sList[0][2]);
		if (bList.size() > 0)
			timeB = processDown(bList, sList[1][2]);
		return timeA > timeB ? timeA : timeB;
	}

	// 해당 리스트의 사람들이 모두 내려가는 처리(소요시간 계산)
	private static int processDown(ArrayList<Person> list, int height) {
		Collections.sort(list);
		int time = list.get(0).time;
		int size = list.size();
		int ingCnt = 0, cCnt = 0;
		Person p;
		while (true) {
			for (int i = 0; i < size; i++) {
				p = list.get(i);
				if (p.status == C)
					continue;
				if (p.time == time) {
					p.status = W;

				} else if (p.status == W && ingCnt < 3) {
					p.status = D;
					p.downCnt = 1;
					ingCnt++;
				} else if (p.status == D) {
					if (p.downCnt < height) {
						p.downCnt++;
					} else {
						p.status = C;
						cCnt++;
						ingCnt--;
					}
				}

			}
			if (cCnt == size)
				break;
			++time;

		}
		return time;
	}
}
