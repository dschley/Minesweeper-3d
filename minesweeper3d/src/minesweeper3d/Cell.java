package minesweeper3d;

public class Cell {
	public int surr;
	public boolean hasBomb;
	public boolean visible;
	public int x;
	public int y;
	public int z;
	
	public Cell(int x, int y, int z){
		surr=0;
		hasBomb=false;
		visible=false;
		this.x=x;
		this.y=y;
		this.z=z;
	}
}
