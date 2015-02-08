import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.Map;

import dto.League.League;
import dto.Stats.*;
import constant.Region;
import dto.Summoner.Summoner;
import main.java.riotapi.RiotApi;
import main.java.riotapi.RiotApiException;

import com.google.gson.Gson;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import com.github.theholywaffle.teamspeak3.StringUtil;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.ClientProperty;
import com.github.theholywaffle.teamspeak3.api.Property;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.ChannelCreateEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelDeletedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelDescriptionEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelPasswordChangedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ServerEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3Listener;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroupClient;
import com.github.theholywaffle.teamspeak3.api.wrapper.Wrapper;

public class TS3_Bot {
	//Tier Group Ids
	static int unRanked = 17;
	static int bronze = 16;
	static int silver = 15;
	static int gold = 13;
	static int platinum = 12;
	static int diamond = 11;
	static int master = 10;
	static int challenger = 9;
		//Special Tourney only groups
	static int TunRanked = 100000000;
	static int Tbronze = 20;
	static int Tsilver = 21;
	static int Tgold = 22;
	static int Tplatinum = 23;
	static int Tdiamond = 24;
	static int Tmaster = 25;
	static int Tchallenger = 26;
		//Capital is tourney group
	static int Team1 = 27;
	static int Team2 = 28;
	static int Team3 = 29;
	static int Team4 = 30;
	static int Team5 = 31;
	static int Team6 = 32;
	static int Team7 = 33;
	static int Team8 = 34;
	static int Team9 = 35;
	static int Team10 = 36;
	static int Team11 = 37;
	static int Team12 = 38;
	static int Team13 = 39;
	static int Team14 = 40;
	static int Team15 = 41;
	static int Team16 = 42;
	static int Team17 = 43;
	static int Team18 = 44;
	static int Team19 = 45;
	static int Team20 = 46;
	
	//Tier Channel Ids
		//Division Waiting Rooms
	static int bronzeWR = 21;
	static int silverWR = 23;
	static int goldWR = 24;
	static int platWR = 25;
	static int diamondWR = 26;
	static int masterWR = 27;
	static int challengerWR = 28;
		//Tourney Team Channels
	static int team1 = 30;
	static int team2 = 31;
	static int team3 = 32;
	static int team4 = 33;
	static int team5 = 34;
	static int team6 = 35;
	static int team7 = 36;
	static int team8 = 37;
	static int team9 = 38;
	static int team10 = 39;
	static int team11 = 40;
	static int team12 = 41;
	static int team13 = 42;
	static int team14 = 43;
	static int team15 = 44;
	static int team16 = 45;
	static int team17 = 46;
	static int team18 = 47;
	static int team19 = 48;
	static int team20 = 49;
	
		//TeamFull Booleans
	static boolean team1Full = false;
	static boolean team2Full = false;
	static boolean team3Full = false;
	static boolean team4Full = false;
	static boolean team5Full = false;
	static boolean team6Full = false;
	static boolean team7Full = false;
	static boolean team8Full = false;
	static boolean team9Full = false;
	static boolean team10Full = false;
	static boolean team11Full = false;
	static boolean team12Full = false;
	static boolean team13Full = false;
	static boolean team14Full = false;
	static boolean team15Full = false;
	static boolean team16Full = false;
	static boolean team17Full = false;
	static boolean team18Full = false;
	static boolean team19Full = false;
	static boolean team20Full = false;
	
	static boolean forwardChallenger = true;
	static boolean forwardMaster = false;
	static boolean forwardDiamond = false;
	static boolean forwardPlatinum = true;
	static boolean forwardGold = true;
	static boolean forwardSilver = false;
	static boolean forwardBronze = true;
	
	static int teamOn = Team1;
	static boolean sortingForward = true;
		//OtherImportantGroupId
	static int verifiedGroupId = 19;
	static int currentGroup = 0;
	static int lastTeam = Team2;

	public static void main(String[]args){
		new TS3_Bot();
	}
	
	public TS3_Bot() {		
		TS3Config config = new TS3Config();
		config.setHost("10.0.1.222");
		config.setDebugLevel(Level.ALL);
		config.setLoginCredentials("serveradmin", "vfB5e8Yk");

		TS3Query query = new TS3Query(config);
		query.connect();
		
		final TS3Api api = query.getApi();
		api.selectVirtualServerByPort(9987);
		//api.
		api.setNickname("Botbot");
		api.sendServerMessage("Botbot Online!");
		final List<ServerGroupClient> admins = api.getServerGroupClients(6);
		
		api.registerAllEvents();
		api.addTS3Listeners(new TS3Listener() {
			boolean alive = true;
			String ranking1 = "";
			int rankingId1 = 0;
			String rankingUId1 = "";
			int rankingDBId1 = 0;
			String ranking2 = "";
			int rankingId2 = 0;
			String rankingUId2 = "";
			int rankingDBId2 = 0;
			String ranking3 = "";
			int rankingId3 = 0;
			String rankingUId3 = "";
			int rankingDBId3 = 0;
			String ranking4 = "";
			int rankingId4 = 0;
			String rankingUId4 = "";
			int rankingDBId4 = 0;
			String ranking5 = "";
			int rankingId5 = 0;
			String rankingUId5 = "";
			int rankingDBId5 = 0;
			String ranking6 = "";
			int rankingId6 = 0;
			String rankingUId6 = "";
			int rankingDBId6 = 0;
			String serverGroupsByClientId = "";
			String sumName = "";
			String tier = "null";
			boolean exit = false;
			String botName = "Botbot";
			String nameId = "";
			String tourneyStarter = "";
			boolean tourney = false;
			int test = 6;
			int myCId = 0;
			boolean continueTourneyStart = true;
			boolean registerEnabled = false;
			int tourneyUsers = 0;
			int numberOfTeams = 5;
			List list = new ArrayList();
			public int forCount = 0;
			ClientProperty cProp;
			Random rng = new Random();
			
			//TODO 
			
			public void onTextMessage(TextMessageEvent e) {
				String invokerName = e.getInvokerName();
				
				if (e.getInvokerName().toLowerCase().equals(botName)){
					myCId = e.getInvokerId();
				}
				if (alive && !e.getInvokerName().equals(botName)){
					
					if ((e.getMessage().toLowerCase().contains("hello " + botName.toLowerCase()) && e.getTargetMode() == TextMessageTargetMode.CHANNEL) || e.getMessage().toLowerCase().contains("hello botbot")){
						api.sendChannelMessage("Hello " + e.getInvokerName() + "!");
					}
					if (e.getMessage().toLowerCase().contains("!status")){
						api.sendServerMessage("I'm currently alive and well!!!");
					}
					if (e.getMessage().equals("!help")) {
						api.sendPrivateMessage(e.getInvokerId(), "Hello, I'm " + "[b]" + botName + "[/b]");
						api.sendPrivateMessage(e.getInvokerId(), "Your personal assistant");
						api.sendPrivateMessage(e.getInvokerId(), "I can do the following things:");
						api.sendPrivateMessage(e.getInvokerId(), "[color=green][b]!rankme[/b][/color] = Assigns you a ranked group, if registration is open.");
						api.sendPrivateMessage(e.getInvokerId(), "[color=green][b]!credit[/b][/color] = Tells you orme about this bot.");
						//api.sendPrivateMessage(e.getInvokerId(), "");
						if (admins.toString().contains(e.getInvokerUserId())){
							api.sendPrivateMessage(e.getInvokerId(), "[b][color=red]Bot Mod commands only:");
							api.sendPrivateMessage(e.getInvokerId(), "[color=green][b]!kill[/b][/color] = temporarily disables " + botName);
							api.sendPrivateMessage(e.getInvokerId(), "[color=green][b]!revive[/b][/color] = re-enables " + botName + " after some one !kills him");
							api.sendPrivateMessage(e.getInvokerId(), "[color=green][b]!name[/b][/color] = allows you to change the name of " + botName);
							api.sendPrivateMessage(e.getInvokerId(), "[color=green][b]!enabletourney[/b][/color] = [b]MUST[/b] be called before starting a tourney, safety switch.");
							api.sendPrivateMessage(e.getInvokerId(), "[color=green][b]!startthetourney[/b][/color] = Begins the sorting algorithm to start a tourney!!");
							api.sendPrivateMessage(e.getInvokerId(), "[color=green][b]!openregistration[/b][/color] = Allows users to use the !rankme command.");
							api.sendPrivateMessage(e.getInvokerId(), "[color=green][b]!closeregistration[/b][/color] = Disllows users to use the !rankme command.");
						}
					}
					if (e.getMessage().toLowerCase().equals("!credit")){
						api.sendPrivateMessage(e.getInvokerId(), "This bot was created by Norm over the course of 1-2 weeks.");
						api.sendPrivateMessage(e.getInvokerId(), "If you ever see Norm and have an idea or want a new command for the bot, or just"
								+ " want a bot for oyur own TS Server, Norm doesn't bite, just poke/message/or yell at him. He's a cool kid.");
					}
					if (e.getMessage().toLowerCase().contains("hey " + botName.toLowerCase()) && e.getInvokerName().toLowerCase().equals("miyosaki")){
						api.sendChannelMessage("Hey Miyosaki! I'm the coolest bot ever.");
					}
					if (e.getMessage().toLowerCase().contains("!kill") && admins.toString().contains(e.getInvokerUserId())){
						alive = false;
						api.sendServerMessage("I dead.... " + e.getInvokerName() + " Killed me......");
					}
					if (e.getMessage().toLowerCase().equals("!enabletourney") && admins.toString().contains(e.getInvokerUserId())){
						tourney = true;
						api.sendPrivateMessage(e.getInvokerId(), "Tournament enabled!");
					}
					if (e.getMessage().toLowerCase().equals("!tourneyisover") && admins.toString().contains(e.getInvokerUserId())){
						tourney = false;
						api.sendPrivateMessage(e.getInvokerId(), "This may take a few moments, allow me to take out the trash, it was a wild party.");
						emptyServerGroup(verifiedGroupId, api);
						emptyServerGroup(Team1, api);
						emptyServerGroup(Team2, api);
						emptyServerGroup(Team3, api);
						emptyServerGroup(Team4, api);
						emptyServerGroup(Team5, api);
						emptyServerGroup(Team6, api);
						emptyServerGroup(Team7, api);
						emptyServerGroup(Team8, api);
						emptyServerGroup(Team9, api);
						emptyServerGroup(Team10, api);
						emptyServerGroup(Team11, api);
						emptyServerGroup(Team12, api);
						emptyServerGroup(Team13, api);
						emptyServerGroup(Team14, api);
						emptyServerGroup(Team15, api);
						emptyServerGroup(Team16, api);
						emptyServerGroup(Team17, api);
						emptyServerGroup(Team18, api);
						emptyServerGroup(Team19, api);
						emptyServerGroup(Team20, api);
						emptyServerGroup(Tchallenger, api);
						emptyServerGroup(Tmaster, api);
						emptyServerGroup(Tdiamond, api);
						emptyServerGroup(Tplatinum, api);
						emptyServerGroup(Tgold, api);
						emptyServerGroup(Tsilver, api);
						emptyServerGroup(Tbronze, api);
						
						team1Full = false;
						team2Full = false;
						team3Full = false;
						team4Full = false;
						team5Full = false;
						team6Full = false;
						team7Full = false;
						team8Full = false;
						team9Full = false;
						team10Full = false;
						team11Full = false;
						team12Full = false;
						team13Full = false;
						team14Full = false;
				 		team15Full = false;
				 		team16Full = false;
				 		team17Full = false;
				 		team18Full = false;
				 		team19Full = false;
				 		team20Full = false;
						
						api.sendPrivateMessage(e.getInvokerId(), "Tourney has finished");
					}
					if (e.getMessage().toLowerCase().equals("!openregistration") && admins.toString().contains(e.getInvokerUserId())){
						registerEnabled = true;
						api.sendPrivateMessage(e.getInvokerId(), "Registration is open");
					}
					if (e.getMessage().toLowerCase().equals("!closeregistration") && admins.toString().contains(e.getInvokerUserId())){
						registerEnabled = false;
						api.sendPrivateMessage(e.getInvokerId(), "Registration is closed");
					}
					
					//Add more actions while alive above HERE
					//Tourney sorting
					//
					//
					//TODO Finish Capt'n Draft sorting algorithms
					//
					//
					if (e.getInvokerName().toLowerCase().equals(tourneyStarter.toLowerCase())){
						tourneyUsers = api.getServerGroupClients(verifiedGroupId).size();
						
						if (e.getMessage().toLowerCase().equals("snakedraft") || e.getMessage().toLowerCase().equals("snake draft")){
							tourneyStarter = "";
							//SNAKE DRAFT SORTING GOES HERE
							api.sendServerMessage("The tourney has begun!! Captains for the snake draft are now being chosen!!!");
							//TODO Find a cascading message?
							//TODO Find a way for multi line input?????  Got an idea.......
							api.sendPrivateMessage(1, "");
							
						} else if (e.getMessage().toLowerCase().equals("random")){
							tourneyStarter = "";
							//STRATIFIED RANDOM TOURNEY SORT GOES HERE
							api.sendServerMessage("The tourney has begun!! Random teams are now being thrown together!!!");
							//tourneyUsers = api.getChannelGroupClientsByChannelId(3).toString();
							//TODO Add random Banter? Funny.......
							api.sendServerMessage("Ladies and Gents, we have ourselves " + tourneyUsers + " tournement participants tonight!");
							numberOfTeams = tourneyUsers/5;
							api.sendServerMessage("Tonight, we will have " + numberOfTeams + " teams of 5.");
							teamOn = Team1;
							
							lastTeam = findLastTeam(numberOfTeams);
							
							teamOn = snakeRandomSortATier(Tchallenger, numberOfTeams, teamOn, lastTeam, api);
							teamOn = snakeRandomSortATier(Tmaster, numberOfTeams, teamOn, lastTeam, api);
							teamOn = snakeRandomSortATier(Tdiamond, numberOfTeams, teamOn, lastTeam, api);
							teamOn = snakeRandomSortATier(Tplatinum, numberOfTeams, teamOn, lastTeam, api);
							teamOn = snakeRandomSortATier(Tgold, numberOfTeams, teamOn, lastTeam, api);
							teamOn = snakeRandomSortATier(Tsilver, numberOfTeams, teamOn, lastTeam, api);
							teamOn = snakeRandomSortATier(Tbronze, numberOfTeams, teamOn, lastTeam, api);
							
							/*
							randomSortATier(Tchallenger, numberOfTeams, api);
							randomSortATier(Tmaster, numberOfTeams, api);
							randomSortATier(Tdiamond, numberOfTeams, api);
							randomSortATier(Tplatinum, numberOfTeams, api);
							randomSortATier(Tgold, numberOfTeams, api);
							randomSortATier(Tsilver, numberOfTeams, api);
							randomSortATier(Tbronze, numberOfTeams, api);
							*/
							api.sendServerMessage("Random sorting completed! Let the games begin! May the chances ever be in your favor.");
							
							dumpRemainderInSubs(api);
							
						} else if (e.getMessage().toLowerCase().equals("exit")){
							continueTourneyStart = false;
							tourneyStarter = "";
						} else if (continueTourneyStart){
							api.sendPrivateMessage(e.getInvokerId(), "Sorry, you have not entered a valid option, try again or type 'exit'");
						}
					}
					
					//Renaming input process
					if (e.getInvokerName().equals(nameId)){
						botName = e.getMessage();
						api.setNickname(botName);
						api.sendPrivateMessage(e.getInvokerId(), "My new name is " + botName);
						nameId = "";
					}
					//COMMENCE RANKIGN PROCESSSSS Place other commands above here...
					if(ranking1.equals(e.getInvokerName())){
						ranking(ranking1, sumName, rankingId1, rankingDBId1, e, api);
						ranking1 = "";
					}
					if(ranking2.equals(e.getInvokerName())){
						ranking(ranking2, sumName, rankingId2, rankingDBId2, e, api);
						ranking2 = "";
					}
					if(ranking3.equals(e.getInvokerName())){
						ranking(ranking3, sumName, rankingId3, rankingDBId3, e, api);
						ranking3 = "";
					}
					if(ranking4.equals(e.getInvokerName())){
						ranking(ranking4, sumName, rankingId4, rankingDBId4, e, api);
						ranking4 = "";
					}
					if(ranking5.equals(e.getInvokerName())){
						ranking(ranking5, sumName, rankingId5, rankingDBId5, e, api);
						ranking5 = "";
					}
					if(ranking6.equals(e.getInvokerName())){
						ranking(ranking6, sumName, rankingId6, rankingDBId6, e, api);
						ranking6 = "";
					}
						
					//End of Ranking
					boolean continueRankMe = true;
					if (e.getMessage().toLowerCase().contains("!rankme") && registerEnabled){
						if(ranking1.equals("")){
							ranking1 = e.getInvokerName();
							rankingId1 = e.getInvokerId();
							rankingUId1 = e.getInvokerUserId();
							rankingDBId1 = api.getDatabaseClientByUId(rankingUId1).getDatabaseId();
							api.sendPrivateMessage(rankingId1, "You are now #1 in the ranking queue");
						} else if(ranking2.equals("")){
							ranking2 = e.getInvokerName();
							rankingId2 = e.getInvokerId();
							rankingUId2 = e.getInvokerUserId();
							rankingDBId2 = api.getDatabaseClientByUId(rankingUId2).getDatabaseId();
							api.sendPrivateMessage(rankingId2, "You are now #2 in the ranking queue");
						} else if(ranking3.equals("")){
							ranking3 = e.getInvokerName();
							rankingId3 = e.getInvokerId();
							rankingUId3 = e.getInvokerUserId();
							rankingDBId3 = api.getDatabaseClientByUId(rankingUId3).getDatabaseId();
							api.sendPrivateMessage(rankingId3, "You are now #3 in the ranking queue");
						} else if(ranking4.equals("")){
							ranking4 = e.getInvokerName();
							rankingId4 = e.getInvokerId();
							rankingUId4 = e.getInvokerUserId();
							rankingDBId4 = api.getDatabaseClientByUId(rankingUId4).getDatabaseId();
							api.sendPrivateMessage(rankingId4, "You are now #4 in the ranking queue");
						} else if(ranking5.equals("")){
							ranking5 = e.getInvokerName();
							rankingId5 = e.getInvokerId();
							rankingUId5 = e.getInvokerUserId();
							rankingDBId5 = api.getDatabaseClientByUId(rankingUId5).getDatabaseId();
							api.sendPrivateMessage(rankingId5, "You are now #5 in the ranking queue");
						} else if(ranking6.equals("")){
							ranking6 = e.getInvokerName();
							rankingId6 = e.getInvokerId();
							rankingUId6 = e.getInvokerUserId();
							rankingDBId6 = api.getDatabaseClientByUId(rankingUId6).getDatabaseId();
							api.sendPrivateMessage(rankingId6, "You are now #6 in the ranking queue");
						} else {
							api.sendPrivateMessage(e.getInvokerId(), "Soryy, we're ranking at full capacity at the moment, try again in 10 seconds. Thank You.");
							continueRankMe = false;
						}
						if (continueRankMe){
							api.sendPrivateMessage(e.getInvokerId(), "Please enter your League of Legends summoner name (not login and NA server only):");
							api.sendPrivateMessage(e.getInvokerId(), "Or type 'exit' to exit");
						}
					}else if (e.getMessage().toLowerCase().equals("!rankme") && !registerEnabled){
						api.sendPrivateMessage(e.getInvokerId(), "Sorry, registration is not open at the moment.");
					}
					if (e.getMessage().toLowerCase().contains("!name") && admins.toString().contains(e.getInvokerUserId())){
						nameId = e.getInvokerName();
						api.sendPrivateMessage(e.getInvokerId(), "Yay! A name change! Please enter my new name:");
					}
					if (e.getMessage().toLowerCase().contains("!startthetourney") && admins.toString().contains(e.getInvokerUserId()) && tourney){
						api.sendPrivateMessage(e.getInvokerId(), "Awesome! Tourrneeyyy TIMEEEEEEEEEE!! Select a mode, type:");
						api.sendPrivateMessage(e.getInvokerId(), "[b]Snake Draft[/b] = Random Captians assigned and then allowed to draf platyers to their teams");
						api.sendPrivateMessage(e.getInvokerId(), "[b]Random[/b] = Random (Each team will (generally) contain a Bronze, Silver, Gold, Plat and Diamond/Master/Challenger");
						tourneyStarter = e.getInvokerName();
						continueTourneyStart = true;
					} else if (e.getMessage().toLowerCase().equals("!startthetourney") && admins.toString().contains(e.getInvokerUserId()) && !tourney){
						api.sendPrivateMessage(e.getInvokerId(), "Sorry, a tourney must either be in process, isn't enabled (!enabletourney), or someone has yet to clean up from the last by saying !tourneyisover");
					}
					//Add more actions while alive above HERE
				}else if (e.getMessage().toLowerCase().contains("!revive") && admins.toString().contains(e.getInvokerUserId()) && !e.getInvokerName().toLowerCase().equals(botName.toLowerCase())){
					alive = true;
					api.sendServerMessage("I'm alive! Thank you " + e.getInvokerName() + " for !reviving me!");
				}else {
					if (e.getMessage().toLowerCase().contains("!status")){
						api.sendServerMessage("I'm currently dead.....");
					}
				}
			}

			public void onServerEdit(ServerEditedEvent e) {

			}

			public void onClientMoved(ClientMovedEvent e) {

			}

			public void onClientLeave(ClientLeaveEvent e) {

			}

			public void onClientJoin(ClientJoinEvent e) {
				api.sendChannelMessage("Welcome to the Server " + /*e.getClientNickname() + */". Type !help for help!");
			}

			public void onChannelEdit(ChannelEditedEvent e) {

			}

			public void onChannelDescriptionChanged(
					ChannelDescriptionEditedEvent e) {

			}

			public void onChannelCreate(ChannelCreateEvent e) {
				
			}

			public void onChannelDeleted(ChannelDeletedEvent e) {
				
			}

			public void onChannelMoved(ChannelMovedEvent e) {
				
			}

			public void onChannelPasswordChanged(ChannelPasswordChangedEvent e) {
				
			}
		});
	}

	public void emptyServerGroup (int currentGroup, TS3Api api) {
		int size = api.getServerGroupClients(currentGroup).size();
		for (int forCount = 0; forCount < size;) {
			api.removeClientFromServerGroup(currentGroup, api.getServerGroupClients(currentGroup).get(0).getClientDatabaseId());
			forCount++;
		}
	}	
	public void randomSortATier (int currentGroup, int numberOfTeams , TS3Api api) {
		//TODO Fix old rando sorting to 20 tems? Not used though......
		Random rng = new Random();
		//List<?> list = api.getServerGroupClients(currentGroup).g;
		boolean teamsFull = false;
		if(team1Full && team2Full && team3Full && team4Full && team5Full && team6Full && team7Full && team8Full &&
				 team9Full && team10Full && team11Full && team12Full && team13Full && team14Full && team15Full && team16Full
				 && team17Full && team18Full && team19Full && team20Full){
			teamsFull = true;
		}
		while (api.getServerGroupClients(currentGroup).size() > 0 && !teamsFull){
			//list = api.getServerGroupClients(currentGroup);
			List<ServerGroupClient> serverGroup = api.getServerGroupClients(currentGroup);
			Collections.shuffle(serverGroup);
			int x = rng.nextInt(numberOfTeams) + 1;
			
			switch (x) {
				case 1: if (!team1Full && api.getServerGroupClients(Team1).size() < 5 && numberOfTeams >= 1) {
					api.addClientToServerGroup(Team1, serverGroup.get(0).getClientDatabaseId());
					api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team1);
					api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				} else {
					team1Full = true;
				}
				break;
				case 2: if (!team2Full && api.getServerGroupClients(Team2).size() < 5 && numberOfTeams >= 2) {
					api.addClientToServerGroup(Team2, serverGroup.get(0).getClientDatabaseId());
					api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team2);
					api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				} else {
					team2Full = true;
				}
				break;
				case 3: if (!team3Full && api.getServerGroupClients(Team3).size() < 5 && numberOfTeams >= 3) {
					api.addClientToServerGroup(Team3, serverGroup.get(0).getClientDatabaseId());
					api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team3);
					api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				} else {
					team3Full = true;
				}
				break;
				case 4: if (!team4Full && api.getServerGroupClients(Team4).size() < 5 && numberOfTeams >= 4) {
					api.addClientToServerGroup(Team4, serverGroup.get(0).getClientDatabaseId());
					api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team4);
					api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				} else {
					team4Full = true;
				}
				break;
				case 5: if (!team5Full && api.getServerGroupClients(Team5).size() < 5 && numberOfTeams >= 5) {
					api.addClientToServerGroup(Team5, serverGroup.get(0).getClientDatabaseId());
					api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team5);
					api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				} else {
					team5Full = true;
				}
				break;
				case 6: if (!team6Full && api.getServerGroupClients(Team6).size() < 5 && numberOfTeams >= 6) {
					api.addClientToServerGroup(Team6, serverGroup.get(0).getClientDatabaseId());
					api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team6);
					api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				} else {
					team6Full = true;
				}
				break;
				case 7: if (!team7Full && api.getServerGroupClients(Team7).size() < 5 && numberOfTeams >= 7) {
					api.addClientToServerGroup(Team7, serverGroup.get(0).getClientDatabaseId());
					api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team7);
					api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				} else {
					team7Full = true;
				}
				break;
				case 8: if (!team8Full && api.getServerGroupClients(Team8).size() < 5 && numberOfTeams >= 8) {
					api.addClientToServerGroup(Team8, serverGroup.get(0).getClientDatabaseId());
					api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team8);
					api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				} else {
					team8Full = true;
				}
				break;
				case 9: if (!team9Full && api.getServerGroupClients(Team9).size() < 5 && numberOfTeams >= 9) {
					api.addClientToServerGroup(Team9, serverGroup.get(0).getClientDatabaseId());
					api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team9);
					api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				} else {
					team9Full = true;
				}
				break;
				case 10: if (!team10Full && api.getServerGroupClients(Team10).size() < 5 && numberOfTeams >= 10) {
					api.addClientToServerGroup(Team10, serverGroup.get(0).getClientDatabaseId());
					api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team10);
					api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				} else {
					team10Full = true;
				}
				break;
				case 11: if (!team11Full && api.getServerGroupClients(Team11).size() < 5 && numberOfTeams >= 11) {
					api.addClientToServerGroup(Team11, serverGroup.get(0).getClientDatabaseId());
					api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team11);
					api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				} else {
					team11Full = true;
				}
				break;
				case 12: if (!team12Full && api.getServerGroupClients(Team12).size() < 5 && numberOfTeams >= 12) {
					api.addClientToServerGroup(Team12, serverGroup.get(0).getClientDatabaseId());
					api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team12);
					api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				} else {
					team12Full = true;
				}
				break;
				case 13: if (!team13Full && api.getServerGroupClients(Team13).size() < 5 && numberOfTeams >= 13) {
					api.addClientToServerGroup(Team13, serverGroup.get(0).getClientDatabaseId());
					api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team13);
					api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				} else {
					team13Full = true;
				}
				break;
				case 14: if (!team14Full && api.getServerGroupClients(Team14).size() < 5 && numberOfTeams >= 14) {
					api.addClientToServerGroup(Team14, serverGroup.get(0).getClientDatabaseId());
					api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team14);
					api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				} else {
					team14Full = true;
				}
				break;
				case 15: if (!team15Full && api.getServerGroupClients(Team15).size() < 5 && numberOfTeams >= 15) {
					api.addClientToServerGroup(Team15, serverGroup.get(0).getClientDatabaseId());
					api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team15);
					api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				} else {
					team15Full = true;
				}
				break;
				
			}
		}
	}
	public void ranking(String ranking, String sumName, int rankingId, int rankingDBId, TextMessageEvent e, TS3Api api){
		boolean exit = false;
		if (e.getMessage().toLowerCase().contains("exit")){
			exit = true;
		}
		if (!exit){
			sumName = e.getMessage();
			api.sendPrivateMessage(e.getInvokerId(), "Finding ranking for " + sumName + ". If there's "
					+ "an error, or it takes longer than 10 seconds, type \"exit\" and try again.");
			String tier = "Bronze";
			String serverGroupsByClientId = "";
			try {
				tier = RiotApitierGet.GetTier(sumName);
				//api.sendPrivateMessage(e.getInvokerId(), tier);
			} catch (RiotApiException e1) {
				api.sendPrivateMessage(rankingId, "Sorry we were unable to get your ranking. Are you sure you spelled your summoner name correctly? (different from login name)");
			} catch (UnsupportedEncodingException e1) {
				api.sendPrivateMessage(rankingId, "Sorry, something terribble has occured. Try again in a few seconds");
				//In case of weird formatting oops?
			} catch (IOException e1) {
				api.sendPrivateMessage(rankingId, "Sorry, something terribble has occured. Try again in a few seconds");
				//How?
			}
			serverGroupsByClientId = api.getServerGroupsByClientId(rankingDBId).toString();
			
			if (serverGroupsByClientId.contains("Challenger")){
				api.removeClientFromServerGroup(challenger, rankingDBId);
				api.removeClientFromServerGroup(Tchallenger, rankingDBId);
			} else if (serverGroupsByClientId.contains("Master")){
				api.removeClientFromServerGroup(master, rankingDBId);
				api.removeClientFromServerGroup(Tmaster, rankingDBId);
			} else if (serverGroupsByClientId.contains("Diamond")){
				api.removeClientFromServerGroup(diamond, rankingDBId);
				api.removeClientFromServerGroup(Tdiamond, rankingDBId);
			} else if (serverGroupsByClientId.contains("Platinum")){
				api.removeClientFromServerGroup(platinum, rankingDBId);
				api.removeClientFromServerGroup(Tplatinum, rankingDBId);
			} else if (serverGroupsByClientId.contains("Gold")){
				api.removeClientFromServerGroup(gold, rankingDBId);
				api.removeClientFromServerGroup(Tgold, rankingDBId);
			} else if (serverGroupsByClientId.contains("Silver")){
				api.removeClientFromServerGroup(silver, rankingDBId);
				api.removeClientFromServerGroup(Tsilver, rankingDBId);
			} else if (serverGroupsByClientId.contains("Bronze")){
				api.removeClientFromServerGroup(bronze, rankingDBId);
				api.removeClientFromServerGroup(Tbronze, rankingDBId);
			} else if (serverGroupsByClientId.contains("Unranked")){
				api.removeClientFromServerGroup(unRanked, rankingDBId);
				api.removeClientFromServerGroup(TunRanked, rankingDBId);
			}
			
			if (tier.contains("Challenger")){
				api.addClientToServerGroup(challenger, rankingDBId);
				api.addClientToServerGroup(Tchallenger, rankingDBId);
				api.moveClient(e.getInvokerId(), challengerWR);
			}else if (tier.contains("Master")){
				api.addClientToServerGroup(master, rankingDBId);
				api.addClientToServerGroup(Tmaster, rankingDBId);
				api.moveClient(e.getInvokerId(), masterWR);
			}else if (tier.contains("Diamond")){
				api.addClientToServerGroup(diamond, rankingDBId);
				api.addClientToServerGroup(Tdiamond, rankingDBId);
				api.moveClient(e.getInvokerId(), diamondWR);
			}else if (tier.contains("Platinum")){
				api.addClientToServerGroup(platinum, rankingDBId);
				api.addClientToServerGroup(Tplatinum, rankingDBId);
				api.moveClient(e.getInvokerId(), platWR);
			}else if (tier.contains("Gold")){
				api.addClientToServerGroup(gold, rankingDBId);
				api.addClientToServerGroup(Tgold, rankingDBId);
				api.moveClient(e.getInvokerId(), goldWR);
			}else if (tier.contains("Silver")){
				api.addClientToServerGroup(silver, rankingDBId);
				api.addClientToServerGroup(Tsilver, rankingDBId);
				api.moveClient(e.getInvokerId(), silverWR);
			}else if (tier.contains("Bronze")){
				api.addClientToServerGroup(bronze, rankingDBId);
				api.addClientToServerGroup(Tbronze, rankingDBId);
				api.moveClient(e.getInvokerId(), bronzeWR);
			}else if (tier.contains("null") || tier.contains("")){
				api.addClientToServerGroup(unRanked, rankingDBId);
			}
			
			api.addClientToServerGroup(verifiedGroupId, rankingDBId);
			api.sendPrivateMessage(e.getInvokerId(), "Your Highest achieved ranked tier in this season or last is: " + tier);
			//api.sendPrivateMessage(e.getInvokerId(), tier);
			api.sendPrivateMessage(e.getInvokerId(), "You can dispute this if you disagree by contacting an admin. Thank you.");
		}
	}
	public int snakeRandomSortATier (int currentGroup, int numberOfTeams, int teamOn, int lastTeam, TS3Api api) {
		List<ServerGroupClient> serverGroup = api.getServerGroupClients(currentGroup);
		Collections.shuffle(serverGroup);
		boolean teamsFull = false;
		while(api.getServerGroupClients(currentGroup).size() > 0 && !teamsFull){
			
			if(team1Full && team2Full && team3Full && team4Full && team5Full && team6Full && team7Full && team8Full &&
					 team9Full && team10Full && team11Full && team12Full && team13Full && team14Full && team15Full && team16Full
					 && team17Full && team18Full && team19Full && team20Full){
				teamsFull = true;
			}//TODO what if not 15 teams? No teamfull
			
			//Tem 1
			/*
			if (api.getServerGroupClients(Team1).size() >= 5){
				team1Full = true;
			}
			*/
			
			api.sendServerMessage("We are on Team: " + teamOn);
			//TODO Team One isn't throwing the snake back correctly.....
			if (!team1Full && teamOn == Team1 && numberOfTeams >= 1) {
				serverGroup = api.getServerGroupClients(currentGroup);
				api.addClientToServerGroup(Team1, serverGroup.get(0).getClientDatabaseId());
				api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team1);
				api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				if (teamOn != lastTeam && sortingForward){
					teamOn = Team2;
				} else if (teamOn != lastTeam && !sortingForward){
					teamOn = Team1;
					sortingForward = true;
					//Tem 1 switchback
				} else if (teamOn == lastTeam && sortingForward){
					sortingForward = false;
					teamOn = Team1;
					//Not gunna happen????
				} else if (teamOn == lastTeam && !sortingForward){
					teamOn = Team1;
					sortingForward = true;
					//Also not gunna happen???
				}
			}
			//Tem 2
			if (api.getServerGroupClients(Team2).size() >= 5){
				team2Full = true;
			}
			
			if (!team2Full && teamOn == Team2 && numberOfTeams >= 2) {
				serverGroup = api.getServerGroupClients(currentGroup);
				api.addClientToServerGroup(Team2, serverGroup.get(0).getClientDatabaseId());
				api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team2);
				api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				if (teamOn != lastTeam && sortingForward){
					teamOn = Team3;
				} else if (teamOn == lastTeam && sortingForward){
					sortingForward = false;
					teamOn = Team2;
				} else if (!sortingForward){
					teamOn = Team1;
				}
			}
			//Tem 3
			if (api.getServerGroupClients(Team3).size() >= 5){
				team3Full = true;
			}
			if (!team3Full && teamOn == Team3 && numberOfTeams >= 3) {
				serverGroup = api.getServerGroupClients(currentGroup);
				api.addClientToServerGroup(Team3, serverGroup.get(0).getClientDatabaseId());
				api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team3);
				api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				if (teamOn != lastTeam && sortingForward){
					teamOn = Team4;
				} else if (teamOn == lastTeam && sortingForward){
					sortingForward = false;
					teamOn = Team3;
				} else if (!sortingForward){
					teamOn = Team2;
				}
			}//Tem 4
			if (api.getServerGroupClients(Team4).size() >= 5){
				team4Full = true;
			}
			if (!team4Full && teamOn == Team4 && numberOfTeams >= 4) {
				serverGroup = api.getServerGroupClients(currentGroup);
				api.addClientToServerGroup(Team4, serverGroup.get(0).getClientDatabaseId());
				api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team4);
				api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				if (teamOn != lastTeam && sortingForward){
					teamOn = Team5;
				} else if (teamOn == lastTeam && sortingForward){
					sortingForward = false;
					teamOn = Team4;
				} else if (!sortingForward){
					teamOn = Team3;
				}
			}//Tem 5
			if (api.getServerGroupClients(Team5).size() >= 5){
				team5Full = true;
			}
			if (!team5Full && teamOn == Team5 && numberOfTeams >= 5) {
				serverGroup = api.getServerGroupClients(currentGroup);
				api.addClientToServerGroup(Team5, serverGroup.get(0).getClientDatabaseId());
				api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team5);
				api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				if (teamOn != lastTeam && sortingForward){
					teamOn = Team6;
				} else if (teamOn == lastTeam && sortingForward){
					sortingForward = false;
					teamOn = Team5;
				} else if (!sortingForward){
					teamOn = Team4;
				}
			}//Tem 6
			if (api.getServerGroupClients(Team6).size() >= 5){
				team6Full = true;
			}
			if (!team6Full && teamOn == Team6 && numberOfTeams >= 6) {
				serverGroup = api.getServerGroupClients(currentGroup);
				api.addClientToServerGroup(Team6, serverGroup.get(0).getClientDatabaseId());
				api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team6);
				api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				if (teamOn != lastTeam && sortingForward){
					teamOn = Team7;
				} else if (teamOn == lastTeam && sortingForward){
					sortingForward = false;
					teamOn = Team6;
				} else if (!sortingForward){
					teamOn = Team5;
				}
			}//Tem 7
			if (api.getServerGroupClients(Team7).size() >= 5){
				team7Full = true;
			}
			if (!team7Full && teamOn == Team7 && numberOfTeams >= 7) {
				serverGroup = api.getServerGroupClients(currentGroup);
				api.addClientToServerGroup(Team7, serverGroup.get(0).getClientDatabaseId());
				api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team7);
				api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				if (teamOn != lastTeam && sortingForward){
					teamOn = Team8;
				} else if (teamOn == lastTeam && sortingForward){
					sortingForward = false;
					teamOn = Team7;
				} else if (!sortingForward){
					teamOn = Team6;
				}
			}//Tem 8
			if (api.getServerGroupClients(Team8).size() >= 5){
				team8Full = true;
			}
			if (!team8Full && teamOn == Team8 && numberOfTeams >= 8) {
				serverGroup = api.getServerGroupClients(currentGroup);
				api.addClientToServerGroup(Team8, serverGroup.get(0).getClientDatabaseId());
				api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team8);
				api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				if (teamOn != lastTeam && sortingForward){
					teamOn = Team9;
				} else if (teamOn == lastTeam && sortingForward){
					sortingForward = false;
					teamOn = Team8;
				} else if (!sortingForward){
					teamOn = Team7;
				}
			}//Tem 9
			if (api.getServerGroupClients(Team9).size() >= 5){
				team9Full = true;
			}
			if (!team9Full && teamOn == Team9 && numberOfTeams >= 9) {
				serverGroup = api.getServerGroupClients(currentGroup);
				api.addClientToServerGroup(Team9, serverGroup.get(0).getClientDatabaseId());
				api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team9);
				api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				if (teamOn != lastTeam && sortingForward){
					teamOn = Team10;
				} else if (teamOn == lastTeam && sortingForward){
					sortingForward = false;
					teamOn = Team9;
				} else if (!sortingForward){
					teamOn = Team8;
				}
			}//Tem 10
			if (api.getServerGroupClients(Team10).size() >= 5){
				team10Full = true;
			}
			if (!team10Full && teamOn == Team10 && numberOfTeams >= 10) {
				serverGroup = api.getServerGroupClients(currentGroup);
				api.addClientToServerGroup(Team10, serverGroup.get(0).getClientDatabaseId());
				api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team10);
				api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				if (teamOn != lastTeam && sortingForward){
					teamOn = Team11;
				} else if (teamOn == lastTeam && sortingForward){
					sortingForward = false;
					teamOn = Team10;
				} else if (!sortingForward){
					teamOn = Team9;
				}
			}//Tem 11
			if (api.getServerGroupClients(Team11).size() >= 5){
				team11Full = true;
			}
			if (!team11Full && teamOn == Team11 && numberOfTeams >= 11) {
				serverGroup = api.getServerGroupClients(currentGroup);
				api.addClientToServerGroup(Team11, serverGroup.get(0).getClientDatabaseId());
				api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team11);
				api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				if (teamOn != lastTeam && sortingForward){
					teamOn = Team12;
				} else if (teamOn == lastTeam && sortingForward){
					sortingForward = false;
					teamOn = Team11;
				} else if (!sortingForward){
					teamOn = Team10;
				}
			}//Tem 12
			if (api.getServerGroupClients(Team12).size() >= 5){
				team12Full = true;
			}
			if (!team12Full && teamOn == Team12 && numberOfTeams >= 12) {
				serverGroup = api.getServerGroupClients(currentGroup);
				api.addClientToServerGroup(Team12, serverGroup.get(0).getClientDatabaseId());
				api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team12);
				api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				if (teamOn != lastTeam && sortingForward){
					teamOn = Team13;
				} else if (teamOn == lastTeam && sortingForward){
					sortingForward = false;
					teamOn = Team12;
				} else if (!sortingForward){
					teamOn = Team11;
				}
			}//Tem 13
			if (api.getServerGroupClients(Team13).size() >= 5){
				team13Full = true;
			}
			if (!team13Full && teamOn == Team13 && numberOfTeams >= 13) {
				serverGroup = api.getServerGroupClients(currentGroup);
				api.addClientToServerGroup(Team13, serverGroup.get(0).getClientDatabaseId());
				api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team13);
				api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				if (teamOn != lastTeam && sortingForward){
					teamOn = Team14;
				} else if (teamOn == lastTeam && sortingForward){
					sortingForward = false;
					teamOn = Team13;
				} else if (!sortingForward){
					teamOn = Team12;
				}
			}//Tem 14
			if (api.getServerGroupClients(Team14).size() >= 5){
				team14Full = true;
			}
			if (!team14Full && teamOn == Team14 && numberOfTeams >= 14) {
				serverGroup = api.getServerGroupClients(currentGroup);
				api.addClientToServerGroup(Team14, serverGroup.get(0).getClientDatabaseId());
				api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team14);
				api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				if (teamOn != lastTeam && sortingForward){
					teamOn = Team15;
				} else if (teamOn == lastTeam && sortingForward){
					sortingForward = false;
					teamOn = Team14;
				} else if (!sortingForward){
					teamOn = Team13;
				}
			}//Tem 15
			if (api.getServerGroupClients(Team15).size() >= 5){
				team15Full = true;
			}
			if (!team15Full && teamOn == Team15 && numberOfTeams >= 14) {
				serverGroup = api.getServerGroupClients(currentGroup);
				api.addClientToServerGroup(Team15, serverGroup.get(0).getClientDatabaseId());
				api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team15);
				api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				if (teamOn != lastTeam && sortingForward){
					teamOn = Team16;
				} else if (teamOn == lastTeam && sortingForward){
					sortingForward = false;
					teamOn = Team15;
				} else if (!sortingForward){
					teamOn = Team14;
				}
			}//Tem 16
			if (api.getServerGroupClients(Team16).size() >= 5){
				team16Full = true;
			}
			if (!team16Full && teamOn == Team16 && numberOfTeams >= 14) {
				serverGroup = api.getServerGroupClients(currentGroup);
				api.addClientToServerGroup(Team16, serverGroup.get(0).getClientDatabaseId());
				api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team16);
				api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				if (teamOn != lastTeam && sortingForward){
					teamOn = Team17;
				} else if (teamOn == lastTeam && sortingForward){
					sortingForward = false;
					teamOn = Team16;
				} else if (!sortingForward){
					teamOn = Team15;
				}
			}//Tem 17
			if (api.getServerGroupClients(Team17).size() >= 5){
				team17Full = true;
			}
			if (!team17Full && teamOn == Team17 && numberOfTeams >= 14) {
				serverGroup = api.getServerGroupClients(currentGroup);
				api.addClientToServerGroup(Team17, serverGroup.get(0).getClientDatabaseId());
				api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team17);
				api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				if (teamOn != lastTeam && sortingForward){
					teamOn = Team18;
				} else if (teamOn == lastTeam && sortingForward){
					sortingForward = false;
					teamOn = Team17;
				} else if (!sortingForward){
					teamOn = Team16;
				}
			}//Tem 18
			if (api.getServerGroupClients(Team18).size() >= 5){
				team18Full = true;
			}
			if (!team18Full && teamOn == Team18 && numberOfTeams >= 14) {
				serverGroup = api.getServerGroupClients(currentGroup);
				api.addClientToServerGroup(Team18, serverGroup.get(0).getClientDatabaseId());
				api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team18);
				api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				if (teamOn != lastTeam && sortingForward){
					teamOn = Team19;
				} else if (teamOn == lastTeam && sortingForward){
					sortingForward = false;
					teamOn = Team18;
				} else if (!sortingForward){
					teamOn = Team17;
				}
			}//Tem 19
			if (api.getServerGroupClients(Team19).size() >= 5){
				team19Full = true;
			}
			if (!team19Full && teamOn == Team19 && numberOfTeams >= 14) {
				serverGroup = api.getServerGroupClients(currentGroup);
				api.addClientToServerGroup(Team19, serverGroup.get(0).getClientDatabaseId());
				api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team19);
				api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				if (teamOn != lastTeam && sortingForward){
					teamOn = Team20;
				} else if (teamOn == lastTeam && sortingForward){
					sortingForward = false;
					teamOn = Team19;
				} else if (!sortingForward){
					teamOn = Team18;
				}
			}//Tem 20
			if (api.getServerGroupClients(Team20).size() >= 5){
				team20Full = true;
			}
			if (!team20Full && teamOn == Team20 && numberOfTeams >= 15) {
				serverGroup = api.getServerGroupClients(currentGroup);
				api.addClientToServerGroup(Team20, serverGroup.get(0).getClientDatabaseId());
				api.moveClient(getCLIDFromNick(serverGroup.get(0).getNickname(), api), team20);
				api.removeClientFromServerGroup(currentGroup, serverGroup.get(0).getClientDatabaseId());
				if (teamOn != lastTeam && sortingForward){
					teamOn = Team20;
				} else if (teamOn == lastTeam && sortingForward){
					sortingForward = false;
					teamOn = Team20;
				} else if (!sortingForward){
					teamOn = Team19;
				} else if (teamOn == lastTeam && !sortingForward){
					teamOn = Team19;
				}
			}
		}
		return teamOn;
	}
	public void dumpRemainderInSubs(TS3Api api){
		
	}
	public int getCLIDFromNick(String clientNick, TS3Api api){
		String line = api.getClientByName(clientNick).toString();
		
		int whereIsCLID = line.indexOf("clid=") + 5;
		int CLIDEnds = line.indexOf(", client_country=");
		String stringCLID = line.substring(whereIsCLID, CLIDEnds);
		int CLID = Integer.parseInt(stringCLID);
		
		
		return CLID;
	}
	public int findLastTeam(int numberOfTeams){
		int lastTeam = Team2;
		if (numberOfTeams == 1){
			lastTeam = Team1;
		} else if (numberOfTeams == 2){
			lastTeam = Team2;
		} else if (numberOfTeams == 3){
			lastTeam = Team3;
		} else if (numberOfTeams == 4){
			lastTeam = Team4;
		} else if (numberOfTeams == 5){
			lastTeam = Team5;
		} else if (numberOfTeams == 6){
			lastTeam = Team6;
		} else if (numberOfTeams == 7){
			lastTeam = Team7;
		} else if (numberOfTeams == 8){
			lastTeam = Team8;
		} else if (numberOfTeams == 9){
			lastTeam = Team9;
		} else if (numberOfTeams == 10){
			lastTeam = Team10;
		} else if (numberOfTeams == 11){
			lastTeam = Team11;
		} else if (numberOfTeams == 12){
			lastTeam = Team12;
		} else if (numberOfTeams == 13){
			lastTeam = Team13;
		} else if (numberOfTeams == 14){
			lastTeam = Team14;
		} else if (numberOfTeams == 15){
			lastTeam = Team15;
		} else if (numberOfTeams == 16){
			lastTeam = Team16;
		} else if (numberOfTeams == 17){
			lastTeam = Team17;
		} else if (numberOfTeams == 18){
			lastTeam = Team18;
		} else if (numberOfTeams == 19){
			lastTeam = Team19;
		} else if (numberOfTeams == 20){
			lastTeam = Team20;
		}
		
		return lastTeam;
	}
}
