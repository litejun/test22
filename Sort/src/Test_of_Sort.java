import java.util.Scanner;

import Olognsort.Heap_sort;
import Olognsort.Merge_sort;
import Olognsort.Quick_sort;
import On2sort.Bubble_sort;
import On2sort.Insert_sort;
import On2sort.Select_sort;

public class Test_of_Sort {
	public static void main(String args[]){
		Scanner scan = new Scanner(System.in);
		
		int temp, size;
		System.out.println("배열의 크기를 입력하세요");
		size = scan.nextInt();
		int[] array = new int[size];
		
			for(int i = 0; i<array.length;i++){
				array[i] = (int)(Math.random()*1000);
			}

			for(int i=0;i<array.length;i++){
				System.out.print(array[i] + " ");
			}
		System.out.println();
		
		
		//종류별 소트 불러오기(O(n2))
		Bubble_sort bubble = new Bubble_sort();
		Insert_sort insert = new Insert_sort();
		Select_sort select = new Select_sort();
		//종류별 소트 불러오기(O(logn))
		Heap_sort heap = new Heap_sort();
		Merge_sort merge = new Merge_sort();
		Quick_sort quick = new Quick_sort();
		
		long start = System.currentTimeMillis();
		System.out.println("버블 정렬을 시작합니다.");
		bubble.bubble(array);
		System.out.println("버블정렬 완료");
		long end = System.currentTimeMillis();
		
		
		long start2 = System.currentTimeMillis();
		System.out.println("삽입 정렬을 시작합니다.");
		insert.insert(array);
		System.out.println("삽입 정렬 완료");
		long end2 = System.currentTimeMillis();
		
		
		long start3 = System.currentTimeMillis();
		System.out.println("선택 정렬을 시작합니다.");
		select.select(array);
		System.out.println("선택 정렬 완료");
		long end3 = System.currentTimeMillis();
		
		
		long start4 = System.currentTimeMillis();
		System.out.println("힙 정렬을 시작합니다.");
		heap.heap(array);
		System.out.println("힙 정렬 완료");
		long end4 = System.currentTimeMillis();
		
		
		long start5 = System.currentTimeMillis();
			System.out.println("병합 정렬을 시작합니다");
		merge.main(array);
		        System.out.println("병합정렬 끝");
		long end5 = System.currentTimeMillis();
		
		long start6 = System.currentTimeMillis();
			System.out.println("퀵 정렬을 시작합니다 ");
        quick.main(array);
        	System.out.println("퀵 정렬 끝");
		long end6 = System.currentTimeMillis();
		
		
		System.out.println();
		System.out.println("버블 : " + (end-start)/1000.0);
		System.out.println("삽입 : " + (end2-start2)/1000.0);
		System.out.println("선택 : " + (end3-start3)/1000.0);
		System.out.println("힙 : " + (end4-start4)/1000.0);
		System.out.println("병합 : " + (end5-start5)/1000.0);
		System.out.println("퀵 : " + (end6-start6)/1000.0);
		
		
	}
}