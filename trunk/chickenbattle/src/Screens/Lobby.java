package Screens;

/**********
 * F�r inte servern att fungera och uppdatera clienter uppkopplade, 
 * d� gameclient initieras n�r application startar. Att starta
 * den vid lobbyn skapar en hel del logiska problem.
 * F�r hitta n�got s�tt att g� runt det.
 **********/



import network.GameClient;
import network.GameServer;
import network.Player;
import network.Packet.AddServer;
import Spelet.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class Lobby extends Screen{

	public Sprite mainbg;
	public String playerscore;
	public Sprite background;
	public Sprite join, exit, crosshair;
	public Sprite name;
	public Sprite serverbg;
	public SpriteBatch sb;
	public BitmapFont font, fontname;
	public Player[] players;
	public GameServer server;
	public Array<AddServer> serverlist;
	public String playerName;
	public String tempName;
	public String MasterServerIP;
	int oldX;
	int oldY;
	int xpos;
	int ypos;
	float dy;
	boolean write;
	float textWidth, textHeight;
	public Main main;
	int numPlayers;
	int selectedServer;
	public Lobby(Main m) {
		selectedServer = 0;
		dy = 0;
		main = m;
		font = new BitmapFont();
		fontname = new BitmapFont();
		sb = new SpriteBatch();
		players = new Player[10];
		crosshair = new Sprite(new Texture(Gdx.files.internal("data/weapons/crosshairsmaller.png")));
		mainbg = new Sprite(new Texture(Gdx.files.internal("data/mainmenu/bg.png")));
		background = new Sprite(new Texture(Gdx.files.internal("data/mainmenu/lobbybg.png")));
		serverbg = new Sprite(new Texture(Gdx.files.internal("data/mainmenu/serverbg.png")));
		join = new Sprite(new Texture(Gdx.files.internal("data/mainmenu/join.png")));
		exit = new Sprite(new Texture(Gdx.files.internal("data/mainmenu/exit.png")));
		name = new Sprite(new Texture(Gdx.files.internal("data/mainmenu/name.png")));
		MasterServerIP = "192.168.0.101";
		playerName = "anon";
		tempName = "";
		write = false;
	}

	public boolean keyDown(int arg0) {
		if(Gdx.input.isKeyPressed(Input.Keys.P)){
			main.client.getServers();
			serverlist = main.client.serverlist;
			for(int i=0; i < serverlist.size; i++)
				System.out.println(serverlist.get(i).ip);
		}
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
				main.name = playerName;
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
		} else if (serverlist != null && join.getBoundingRectangle().contains(xpos,ypos)) {
			main.client.Disconnect();
			System.out.println("connecting to " + MasterServerIP);
			main.client.Connect(serverlist.get(selectedServer).ip,54555, 54778);
			main.client.AddPlayer(playerName);
			main.setScreen(Main.GAME);
		} else if (name.getBoundingRectangle().contains(xpos,ypos)){
			write = true;
		} else {
			if (serverlist != null) {
				int i = 0;
				for (AddServer as : serverlist) {
					serverbg.setPosition(background.getX(),background.getHeight()+background.getY()-serverbg.getHeight()*(i+1));
					if (serverbg.getBoundingRectangle().contains(xpos, ypos)) {
						selectedServer = i;
						break;
					}
					i++;
				}
			}
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
		fontname.setColor(Color.BLACK);
		if(!write){
			fontname.draw(sb, playerName, 50+name.getWidth()/2-20, 515);
		}
		else{
			fontname.draw(sb, tempName, 50+name.getWidth()/2-20, 515);
		}

		int i = 0;
		if (serverlist != null) {
			for (AddServer as : serverlist) {
				serverbg.setPosition(background.getX(),background.getHeight()+background.getY()-serverbg.getHeight()*(i+1));
				if (selectedServer == i)
					serverbg.draw(sb);
				else
					serverbg.draw(sb,0.7f);
				font.draw(sb, "IP:" + as.ip + " with " + as.online +"/"+as.playercap  , serverbg.getX()+50, serverbg.getY()+80);
				font.draw(sb, "MOTD:" + as.motd , serverbg.getX()+50, serverbg.getY()+60);
				i++;
			}
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
		main.client = new GameClient();
		main.client.Connect(MasterServerIP,50000, 50002);

		oldX = Gdx.graphics.getWidth()/2;
		oldY = Gdx.graphics.getHeight()/2;
		ypos = oldY;
		xpos = oldX;



	}

}
