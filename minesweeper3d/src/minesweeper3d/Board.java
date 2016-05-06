package minesweeper3d;

import java.io.*;
import java.util.*;
import java.lang.*;
public class Board {

	public Cell[][][] board;
	public int x;
	public int y;
	public int z;
	public int mnum;
	
	
	public Board(int x, int y, int z){
		this.x=x;
		this.y=y;
		this.z=z;
		this.board=new Cell[x][y][z];
		this.mnum=(x+y+z)/3;
	}
	
	public static void setBoard(Board b){
		for(int i=0; i<b.x; i++){
			for(int j=0; j<b.y; j++){
				for(int k=0; k<b.z; k++){
					b.board[i][j][k]=new Cell(i,j,k);
				}
			}
		}
	}
	
	public static void printBoard(Board b){
		
		for(int i=0; i<b.x; i++){
			System.out.println("Slice: "+i);
			for(int j=0; j<b.y; j++){
				for(int k=0; k<b.z; k++){
					if(b.board[i][j][k].hasBomb){
						System.out.print("[B]");
					}else{
						System.out.print("["+b.board[i][j][k].surr+"]");
					}
				}
				System.out.println();
			}
			System.out.println();
		}
		
	}
	
	private static boolean hasWon(Board b) {
		int visibleCount=0;
		for(int i=0; i<b.x; i++){
			for(int j=0; j<b.y; j++){
				for(int k=0; k<b.z; k++){
					if(b.board[i][j][k].visible){
						visibleCount++;
					}
				}
			}
		}
		if((b.x*b.y*b.z)-visibleCount>b.mnum){
			return true;
		}
		return false;
	}
	
	public static void printBoardTiles(Board b){
		
		for(int i=0; i<b.x; i++){
			System.out.println("Slice: "+i);
			for(int j=0; j<b.y; j++){
				for(int k=0; k<b.z; k++){
					if(b.board[i][j][k].visible){
						System.out.print("["+b.board[i][j][k].surr+"]");
					}else{
						System.out.print("[ ]");
					}
				}
				System.out.println();
			}
			System.out.println();
		}
		
	}
	
	public static void incNeighbors(Board b, int x, int y, int z){
		for(int i=x-1; i<=x+1; i++){
			for(int j=y-1; j<=y+1; j++){
				for(int k=z-1; k<=z+1; k++){
					try{
						b.board[i][j][k].surr++;
					}catch(IndexOutOfBoundsException e){
						//System.out.println("caught at "+i+", "+j+", "+k);
					}
				}
			}
		}
	}
	
	public static void distributeMines(Board b){
		int rx, ry, rz;
		Random rand=new Random();
		for(int i=0; i<b.mnum; i++){
			rx=rand.nextInt(b.x);
			ry=rand.nextInt(b.y);
			rz=rand.nextInt(b.z);
			if(b.board[rx][ry][rz].hasBomb){
				System.out.println("had to reshuffle at "+i);
				i--;
			}else{
				b.board[rx][ry][rz].hasBomb=true;
				incNeighbors(b,rx,ry,rz);
			}
		}
		System.out.println("inserted "+b.mnum+" bombs");
	}
	
	public static boolean uncover(Board b, int x, int y, int z){
		/*
		 * 
		 * 
		 * Initialize a queue
			If current square is non-mine uncover it and add to queue, otherwise gameover
			Remove a square from queue
			Count mines adjacent to it
			If adjacent mine count is zero, add any adjacent covered squares to queue and uncover them
			Go to step 3 if queue is not empty, otherwise finish
		 * 
		 * 
		 * */
		
		Cell ptr;
		
		//this is a queue
		LinkedList<Cell> q=new LinkedList<Cell>();
		
		q.addLast(b.board[x][y][z]);
		
		if(b.board[x][y][z].hasBomb){
			//game over
			System.out.println("game over");
			return false;
		}
		
		while(!q.isEmpty()){
			ptr=q.poll();			
			ptr.visible=true;
			if(ptr.surr==0 && !ptr.hasBomb){
				for(int i=ptr.x-1; i<=ptr.x+1; i++){
					for(int j=ptr.y-1; j<=ptr.y+1; j++){
						for(int k=ptr.z-1; k<=ptr.z+1; k++){
							try{
								if(!b.board[i][j][k].visible){
									q.addLast(b.board[i][j][k]);
								}
								
							}catch(IndexOutOfBoundsException e){
								//System.out.println("caught at "+i+", "+j+", "+k);
							}
						}
					}
				}
			}
			
			
		}		
		
		return true;
	}
	
	@SuppressWarnings("resource")
	public static void main(String[] args){
		System.out.println("Enter the size of the board: ");
		Scanner s=new Scanner(System.in);
		int n=s.nextInt();
		Board b=new Board(n,n,n);
		int i,j,k;
		
		setBoard(b);
		//printBoard(b);
		distributeMines(b);
		/*printBoard(b);
		printBoardTiles(b);
		*/
		
		do{
			printBoardTiles(b);
			System.out.println("Enter the slice (0 - "+(b.x-1)+"): ");
			i=s.nextInt();
			System.out.println("Enter the row (0 - "+(b.y-1)+"): ");
			j=s.nextInt();
			System.out.println("Enter the column (0 - "+(b.z-1)+"): ");
			k=s.nextInt();
		}while(uncover(b,i,j,k) && !hasWon(b));
		if(hasWon(b)){
			System.out.println("YOU WIN");
		}else{
			System.out.println("YOU LOSE");
		}
		
	}

	
}
