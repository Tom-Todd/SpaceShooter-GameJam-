package org.msquirrel.SpaceShooter.TileMap;

import java.util.Random;

import org.msquirrel.SpaceShooter.Camera;
import org.msquirrel.SpaceShooter.World;
import org.msquirrel.SpaceShooter.Entities.EnemyBase;
import org.msquirrel.SpaceShooter.TileMap.Tiles.Tile;
import org.msquirrel.SpaceShooter.TileMap.Tiles.TileGround;
import org.msquirrel.SpaceShooter.TileMap.Tiles.TileSpace;
import org.msquirrel.SpaceShooter.TileMap.Tiles.TileWall;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

public class Map implements TileBasedMap{
	public Tile[][] map;
	private Camera cam;
	private static int WIDTH;
	private static int HEIGHT;
	public static int TILE_SIZE = 32;
	
	
	public Map(Camera cam) throws SlickException{
		this.cam = cam;
		Bitmap loadmap = mapLoader.loadBitmap("res/map.png");
		WIDTH = loadmap.width;
		HEIGHT = loadmap.height;
		map = new Tile[WIDTH][HEIGHT];
		for(int x = 0;x < WIDTH; x++){
			for(int y = 0;y < HEIGHT; y++){
				map[x][y]= new TileGround(x, y, this.cam);
				switch (loadmap.pixels[(y*WIDTH)+x]){
					case 0xFF999999:
					{
						map[x][y] = new TileWall(x,y,this.cam);
					}
					break;
					case 0xFF000000:
					{
						map[x][y] = new TileSpace(x,y,this.cam);
					}
					break;
					case 0xFF333333:
					{
						map[x][y] = new TileDoor(x,y,this.cam);
					}
				}
			}
		}
	}
	
	public void draw(Graphics g){
		for(int x = 0;x < WIDTH; x++){
			for(int y = 0;y < HEIGHT; y++){
				map[x][y].draw(g);
			}
		}
	}

	public boolean blocked(float nextX, float nextY, float width, float height) {
		int x = (int) (((nextX))/32);
		int y = (int) (((nextY))/32);
		if(x > -1 && x < WIDTH){
			if(y > -1 && y < HEIGHT){
				if(this.map[x][y].isBlocked()){
					return true;
				}
			}
		}
		x = (int) (((nextX+width))/32);
		y = (int) (((nextY))/32);
		if(x > -1 && x < WIDTH){
			if(y > -1 && y < HEIGHT){
				if(this.map[x][y].isBlocked()){
					return true;
				}
			}
		}
		x = (int) (((nextX))/32);
		y = (int) (((nextY+height))/32);
		if(x > -1 && x < WIDTH){
			if(y > -1 && y < HEIGHT){
				if(this.map[x][y].isBlocked()){
					return true;
				}
			}
		}
		x = (int) (((nextX+width))/32);
		y = (int) (((nextY+height))/32);
		if(x > -1 && x < WIDTH){
			if(y > -1 && y < HEIGHT){
				if(this.map[x][y].isBlocked()){
					return true;
				}
			}
		}
		return false;
	}
	
	public void addEnemies(World world) throws SlickException{
		for(int x =0; x < getWidthInTiles(); x++){
			for(int y =0; y < getHeightInTiles(); y++){
				if(map[x][y] != null){
					if(map[x][y] instanceof TileGround){
						Random generator = new Random();
						int r = generator.nextInt(100);
						if(r == 1){
							world.entities.add(new EnemyBase(x*TILE_SIZE, y*TILE_SIZE, world, world.getPlayer()));
						}
					}
				}
			}
		}
	}

	@Override
	public boolean blocked(PathFindingContext pc, int tx, int ty) {
		if(map[tx][ty].isBlocked()){
			return true;
		}
		return false;
	}

	@Override
	public float getCost(PathFindingContext pc, int tx, int ty) {
		return 1;
	}

	@Override
	public int getHeightInTiles() {
		return HEIGHT;
	}

	@Override
	public int getWidthInTiles() {
		return WIDTH;
	}

	@Override
	public void pathFinderVisited(int x, int y) {
		
	}
}
