package On2sort;
import java.util.*;

public class Select_sort {
	public static void select(int array[]){
		
		for(int i = 0; i<array.length;i++){
			array[i] = (int)(Math.random()*1000);
		}

		for(int i=0;i<array.length;i++){
			System.out.print(array[i] + " ");
		}
	System.out.println();

		int min;
		int j;
		int temp=0;

			for(int i=0;i<array.length;i++){
				min = array[i];
				for(j=i;j<array.length-1;j++){
					if(min > array[j+1]){
						min = array[j+1];
						temp = j+1;
					}
				}
				array[temp] = array[i];
				array[i] = min;
			}
		

			for(int i=0;i<array.length;i++){
				System.out.print(array[i] + " ");
			}
			System.out.println();
	}
}
