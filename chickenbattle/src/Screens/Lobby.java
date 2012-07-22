package Screens;

/**********
 * Får inte servern att fungera och uppdatera clienter uppkopplade, 
 * då gameclient initieras när application startar. Att starta
 * den vid lobbyn skapar en hel del logiska problem.
 * Får hitta något sätt att gå runt det.
 **********/



import network.GameClient;
import network.GameServer;
import network.Player;
import Spelet.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Lobby extends Screen{

	public Sprite mainbg;
	public String playerscore;
	public Sprite background;
	public Sprite join, exit, crosshair;
	public SpriteBatch sb;
	public BitmapFont font;
	public Player[] players;
	public GameServer server;
	int oldX;
	int oldY;
	int xpos;
	int ypos;
	public Main main;
	public Lobby(Main m) {
		main = m;
		font = new BitmapFont();
		sb = new SpriteBatch();
		players = new Player[10];
		crosshair = new Sprite(new Texture(Gdx.files.internal("data/crosshairsmaller.png")));
		mainbg = new Sprite(new Texture(Gdx.files.internal("data/mainmenu/bg.png")));
		background = new Sprite(new Texture(Gdx.files.internal("data/mainmenu/lobbybg.png")));
		join = new Sprite(new Texture(Gdx.files.internal("data/mainmenu/join.png")));
		exit = new Sprite(new Texture(Gdx.files.internal("data/mainmenu/exit.png")));
		try{
			players = server.player;
		}
		catch(NullPointerException e){
			e.getStackTrace();
			System.out.println("No players online!");
		}


	}


	public boolean keyDown(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		xpos += (arg0-oldX);
		ypos += (arg1-oldY)*-1;
		oldX = arg0;
		oldY = arg1;
		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		if (exit.getBoundingRectangle().contains(xpos,ypos)) {
			System.exit(0);
		}
		if (join.getBoundingRectangle().contains(xpos,ypos)) {
			main.setScreen(Main.GAME);
		}



		return false;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		mouseMoved(arg0,arg1);
		return false;
	}

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update() {
		Gdx.input.setCursorCatched(true);
		if (exit.getBoundingRectangle().contains(xpos,ypos)) {
			exit.setColor(1f,0f,0f,1);
		}
		else {
			exit.setColor(0f,0f,0f,1);
		}
		if (join.getBoundingRectangle().contains(xpos,ypos)) {
			join.setColor(1f,0f,0f,1);
		}
		
	}

	@Override
	public void render() {
		sb.begin();
		mainbg.draw(sb);
		background.setPosition(Gdx.graphics.getWidth()/2-background.getWidth()/2, 100);
		background.draw(sb,0.95f);
		exit.setPosition(50, 100);
		exit.draw(sb);
		join.setPosition(50, 400);
		join.draw(sb);
		if(players[0] != null){
			for(int i =0; i < players.length; i++){	
				Player x = players[i];
				if(x != null){
					playerscore = x.name +" kills : " +x.kills + " deaths" + x.deaths;
					float textWidth = font.getBounds(playerscore).width;
					float textHeight = font.getBounds(playerscore).height;
					font.draw(sb, playerscore, Gdx.graphics.getWidth()/2 - textWidth/2, 400 - (i*20) + textHeight / 2);
				}
			}
		}
		else{
			String text = "No players online. Be the first to join!";
			float textWidth = font.getBounds(text).width;
			font.draw(sb, "No players online. Be the first to join!", Gdx.graphics.getWidth()/2 - textWidth/2, 460);
		}
		crosshair.setPosition(xpos-crosshair.getWidth()/2,ypos-crosshair.getHeight()/2);
		crosshair.draw(sb);
		sb.end();
	}
	public void refresh() {

	}

	@Override
	public void enter() {
		Gdx.input.setCursorPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		Gdx.input.setInputProcessor(this);
		oldX = Gdx.graphics.getWidth()/2;
		oldY = Gdx.graphics.getHeight()/2;
		ypos = oldY;
		xpos = oldX;

	}

}
