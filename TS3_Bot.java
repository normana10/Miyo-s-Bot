import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
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

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
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

public class TS3_Bot {
	public static void main(String[]args){
		new TS3_Bot();
	}
	
	public TS3_Bot() {
		//Tier Group Ids
		final int unRanked = 17;
		final int bronze = 16;
		final int silver = 15;
		final int gold = 13;
		final int platinum = 12;
		final int diamond = 11;
		final int master = 10;
		final int challenger = 9;
		
		TS3Config config = new TS3Config();
		config.setHost("10.0.1.222");
		config.setDebugLevel(Level.ALL);
		config.setLoginCredentials("serveradmin", "vfB5e8Yk");

		TS3Query query = new TS3Query(config);
		query.connect();
		
		final TS3Api api = query.getApi();
		api.selectVirtualServerByPort(9987);
		api.setNickname("Botbot");
		api.sendServerMessage("Bot Online!");
		final List<ServerGroupClient> admins = api.getServerGroupClients(6);
		
		api.registerAllEvents();
		api.addTS3Listeners(new TS3Listener() {
			boolean alive = true;
			String ranking = "0";
			String sumName = "";
			String tier = "null";
			boolean exit = false;
			String botName = "Botbot";
			String nameId = "";
			boolean tourney = false;
			
			public void onTextMessage(TextMessageEvent e) {
				if (alive && !e.getInvokerName().equals(botName)){
					if (e.getMessage().toLowerCase().contains("hello " + botName.toLowerCase()) && e.getTargetMode() == TextMessageTargetMode.CHANNEL){
						api.sendChannelMessage("Hello " + e.getInvokerName() + "!");
					}
					if (e.getMessage().toLowerCase().contains("!status")){
						api.sendServerMessage("I'm currently alive and well!!!");
					}
					if (e.getMessage().equals("!help")) {
						api.sendPrivateMessage(e.getInvokerId(), "Hello, I'm " + "[b]" + botName + "[/b]");
						api.sendPrivateMessage(e.getInvokerId(), "Your personal assistant");
						api.sendPrivateMessage(e.getInvokerId(), "I can do the following things:");
						api.sendPrivateMessage(e.getInvokerId(), "[color=green][b]!rankme[/color][/b] = Assigns you a ranked group");
						//api.sendPrivateMessage(e.getInvokerId(), "");
						if (admins.toString().contains(e.getInvokerName())){
							api.sendPrivateMessage(e.getInvokerId(), "[b][color=red]Bot Mod commands only:");
							api.sendPrivateMessage(e.getInvokerId(), "[color=green][b]!kill[/b][/color] = temporarily disables " + botName);
							api.sendPrivateMessage(e.getInvokerId(), "[color=green][b]!revive[/color] [/b]= re-enables " + botName + " after some one !kills him");
							api.sendPrivateMessage(e.getInvokerId(), "[color=green][b]!name[/b][/color] = allows you to change the name of " + botName);
							api.sendPrivateMessage(e.getInvokerId(), "[color=green][b]!enabletourney[/color][/b] = [b]MUST[/b] be called before starting a tourney, safety switch.");
							api.sendPrivateMessage(e.getInvokerId(), "[color=green][b]!startthetourney[/b][/color] = Begins the sorting algorithm to start a tourney!!");
						}
					}
					if (e.getMessage().toLowerCase().contains("hey " + botName.toLowerCase()) && e.getInvokerName().toLowerCase().equals("miyosaki")){
						api.sendChannelMessage("Hey Miyosuki! I'm the coolest bot ever.");
					}
					if (e.getMessage().toLowerCase().contains("!kill") && admins.toString().contains(e.getInvokerName())){
						alive = false;
						api.sendServerMessage("I dead.... " + e.getInvokerName() + " Killed me......");
					}
					if (e.getMessage().toLowerCase().contains("!kill") && admins.toString().contains(e.getInvokerName())){
						alive = false;
						api.sendServerMessage("I dead.... " + e.getInvokerName() + " Killed me......");
					}
					if (e.getMessage().toLowerCase().contains("!enabletourney")){
						tourney = true;
					}
					
					//Add more actions while alive above HERE
					//Tourney sorting
					//Renaming input process
					if (e.getInvokerName().equals(nameId)){
						botName = e.getMessage();
						api.setNickname(botName);
						api.sendPrivateMessage(e.getInvokerId(), "My new name is " + botName);
						nameId = "";
					}
					//COMMENCE RANKIGN PROCESSSSS Place other commands above here....
					if (e.getInvokerName().equals(ranking)){
						exit = false;
						if (e.getMessage().toLowerCase().contains("exit")){
							exit = true;
							ranking = "";
						}
						if (!exit){
							sumName = e.getMessage();
							api.sendPrivateMessage(e.getInvokerId(), "Finding ranking for " + sumName);
							try {
								tier = RiotApitierGet.GetTier(sumName);
								//api.sendPrivateMessage(e.getInvokerId(), tier);
							} catch (RiotApiException e1) {
								api.sendPrivateMessage(e.getInvokerId(), "Sorry we were unable to get your ranking. Are you sure you spelled your summoner name correctly? (different from login name)");
							} catch (UnsupportedEncodingException e1) {
								api.sendPrivateMessage(e.getInvokerId(), "Sorry, something terribble has occured. Try again in a few seconds");
								//In case of weird formatting oops?
							} catch (IOException e1) {
								api.sendPrivateMessage(e.getInvokerId(), "Sorry, something terribble has occured. Try again in a few seconds");
								//How?
							}
							
							if (api.getServerGroupsByClientId(e.getInvokerId()).toString().contains("Challenger")){
								api.removeClientFromServerGroup(challenger, e.getInvokerId());
							} else if (api.getServerGroupsByClientId(e.getInvokerId()).toString().contains("Master")){
								api.removeClientFromServerGroup(master, e.getInvokerId());
							} else if (api.getServerGroupsByClientId(e.getInvokerId()).toString().contains("Diamond")){
								api.removeClientFromServerGroup(diamond, e.getInvokerId());
							} else if (api.getServerGroupsByClientId(e.getInvokerId()).toString().contains("Platinum")){
								api.removeClientFromServerGroup(platinum, e.getInvokerId());
							} else if (api.getServerGroupsByClientId(e.getInvokerId()).toString().contains("Gold")){
								api.removeClientFromServerGroup(gold, e.getInvokerId());
							} else if (api.getServerGroupsByClientId(e.getInvokerId()).toString().contains("Silver")){
								api.removeClientFromServerGroup(silver, e.getInvokerId());
							} else if (api.getServerGroupsByClientId(e.getInvokerId()).toString().contains("Bronze")){
								api.removeClientFromServerGroup(bronze, e.getInvokerId());
							} else if (api.getServerGroupsByClientId(e.getInvokerId()).toString().contains("Unranked")){
								api.removeClientFromServerGroup(unRanked, e.getInvokerId());
							}
							
							if (tier.contains("Challenger")){
								api.addClientToServerGroup(challenger, e.getInvokerId());
							}else if (tier.contains("Master")){
								api.addClientToServerGroup(master, e.getInvokerId());
							}else if (tier.contains("Diamond")){
								api.addClientToServerGroup(diamond, e.getInvokerId());
							}else if (tier.contains("Platinum")){
								api.addClientToServerGroup(platinum, e.getInvokerId());
							}else if (tier.contains("Gold")){
								api.addClientToServerGroup(gold, e.getInvokerId());
							}else if (tier.contains("Silver")){
								api.addClientToServerGroup(silver, e.getInvokerId());
							}else if (tier.contains("Bronze")){
								api.addClientToServerGroup(bronze, e.getInvokerId());
							}else if (tier.contains("null") || tier.contains("")){
								api.addClientToServerGroup(unRanked, e.getInvokerId());
							}
							
							api.sendPrivateMessage(e.getInvokerId(), "Your Highest achieved ranked tier in this season and last is: " + tier);
							//api.sendPrivateMessage(e.getInvokerId(), tier);
							api.sendPrivateMessage(e.getInvokerId(), "You can dispute this if you disagree by contacting an admin. Thank you.");
							api.sendServerMessage(e.getInvokerName() + " Has been added to the " + tier + " server group.");
							ranking = "";
						}
					}
					if (e.getMessage().toLowerCase().contains("!rankme")){
						api.sendPrivateMessage(e.getInvokerId(), "Please enter your League of Legends summoner name (not login):");
						api.sendPrivateMessage(e.getInvokerId(), "Or type 'exit' to exit");
						ranking = e.getInvokerName();
					}
					if (e.getMessage().toLowerCase().contains("!name") && admins.toString().toLowerCase().contains(e.getInvokerName().toLowerCase())){
						nameId = e.getInvokerName();
						api.sendPrivateMessage(e.getInvokerId(), "Yay! A name change! Please enter my new name:");
					}
					//Add more actions while alive above HERE
				}else if (e.getMessage().toLowerCase().contains("!revive") && admins.toString().contains(e.getInvokerName()) && !e.getInvokerName().toLowerCase().equals(botName.toLowerCase())){
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
}
