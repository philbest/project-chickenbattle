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
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Lobby extends Screen{

	public Sprite mainbg;
	public String playerscore;
	public Sprite background;
	public Sprite join, exit, crosshair;
	public Sprite name;
	public SpriteBatch sb;
	public BitmapFont font, fontname;
	public Player[] players;
	public GameServer server;
	public String playerName;
	public String tempName;
	int oldX;
	int oldY;
	int xpos;
	int ypos;
	boolean write;
	float textWidth, textHeight;
	public Main main;
	int numPlayers;
	public Lobby(Main m) {
		main = m;
		font = new BitmapFont();
		fontname = new BitmapFont();
		sb = new SpriteBatch();
		players = new Player[10];
		crosshair = new Sprite(new Texture(Gdx.files.internal("data/crosshairsmaller.png")));
		mainbg = new Sprite(new Texture(Gdx.files.internal("data/mainmenu/bg.png")));
		background = new Sprite(new Texture(Gdx.files.internal("data/mainmenu/lobbybg.png")));
		join = new Sprite(new Texture(Gdx.files.internal("data/mainmenu/join.png")));
		exit = new Sprite(new Texture(Gdx.files.internal("data/mainmenu/exit.png")));
		name = new Sprite(new Texture(Gdx.files.internal("data/mainmenu/name.png")));
		try{
			players = server.player;
		}
		catch(NullPointerException e){
			e.getStackTrace();
			System.out.println("No players online!");
		}
		init();
		playerName = "Player"+numPlayers;
		tempName = "";
		write = false;
	}

	public void init(){
		numPlayers = 0;
		try{
			numPlayers = ((Application)main.screens.get(Main.GAME)).clientid+1;
		}
		catch(NullPointerException e){
			e.getStackTrace();
		}
	}

	public boolean keyDown(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char arg0) {
		if(write){
			if(Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)){
				tempName = tempName.substring(0, tempName.length()-1);
			}
			else if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
				write = false;
				playerName = tempName;
				tempName = "";
			}
			else{
				tempName += arg0;
			}

		}
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
		if (name.getBoundingRectangle().contains(xpos,ypos)){
			write = true;
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
		name.setPosition(50, 500);
		name.draw(sb);

		join.setPosition(50, 400);
		join.draw(sb);
		if(players[0] != null){
			for(int i =0; i < players.length; i++){	
				Player x = players[i];
				if(x != null){
					playerscore = x.name +" kills : " +x.kills + " deaths" + x.deaths;
					textWidth = font.getBounds(playerscore).width;
					textHeight = font.getBounds(playerscore).height;
					font.draw(sb, playerscore, Gdx.graphics.getWidth()/2 - textWidth/2, 400 - (i*20) + textHeight / 2);
				}
			}
		}
		else{
			String text = "No players online. Be the first to join!";
			textWidth = font.getBounds(text).width;
			font.draw(sb, text, Gdx.graphics.getWidth()/2 - textWidth/2, 460);
		}
		fontname.setColor(Color.BLACK);
		if(!write){
		fontname.draw(sb, playerName, 50+name.getWidth()/2-20, 515);
		}
		else{
			fontname.draw(sb, tempName, 50+name.getWidth()/2-20, 515);
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
