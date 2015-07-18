package CTF.Teams;

import CTF.CTFManager;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by Michael on 4/14/2015.
 */

public class TeamManager  {

    public Team TeamRed = new Team("Red", Color.RED);
    public Team TeamBlue = new Team("Blue", Color.BLUE);

    //@Override
    public void onEnable() {

        TeamBlue.ResetMembers();
        TeamRed.ResetMembers();
    }


    public boolean executeCommand(CommandSender sender, Command command, String label, String[] args)
    {
            if(args[1].equalsIgnoreCase("Join"))
            {
                JoinTeam(sender, args);
                return true;
            }
            else if(args[1].equalsIgnoreCase("Members"))
            {
                ShowMemberNames(sender,args);
                return true;
            }
            else if(args[1].equalsIgnoreCase("Info"))
            {
                ShowPlayerInfo(sender,args);
                return true;
            }
            else if(args[1].equalsIgnoreCase("Leave"))
            {
                LeaveTeam(sender);
                return true;
            }
            else
            {
                sender.sendMessage("Usage: /CTF Team [Join | Leave | Members]");
                sender.sendMessage("Join [Red | Blue] - Join a team");
                sender.sendMessage("Leave - Leave your team");
                sender.sendMessage("Members [Red | Blue] - Shows all team members");
                return false;
            }

    }

    private boolean JoinTeam(CommandSender sender, String[] args)
    {
        if (args.length < 3)
        {
            if(GetPlayerTeam((Player)sender) == null)
            {
                TeamMember member = new TeamMember((Player) sender);

                if (TeamRed.members.size() > TeamBlue.members.size())
                {
                    return AssignToTeam(TeamRed, member);
                } else if (TeamRed.members.size() < TeamBlue.members.size())
                {
                    return AssignToTeam(TeamBlue, member);
                } else
                {
                    int rndNum = randInt(0, 1);
                    Team rndTeam = rndNum == 1 ? TeamBlue : TeamRed;
                    return AssignToTeam(rndTeam, member);
                }

            }else return false;
        }
        else if (args[2].equalsIgnoreCase("Blue"))
        {
                if(GetPlayerTeam((Player)sender) == null)
                {
                    if(TeamRed.members.size() + 2 > TeamBlue.members.size())
                    {
                        TeamMember member = new TeamMember((Player) sender);
                        return AssignToTeam(TeamBlue, member);
                    }
                    else
                    {
                        String s = ChatColor.DARK_PURPLE + "Team Is Full!";
                        sender.sendMessage(s);
                        return false;
                    }
                }
                else
                {
                    String s = ChatColor.DARK_PURPLE + "Already joined a team!";
                    sender.sendMessage(s);
                    return false;
                }

        }
        else if (args[2].equalsIgnoreCase("Red"))
        {
                if(GetPlayerTeam((Player)sender) == null)
                {
                    if(TeamBlue.members.size() + 2 > TeamRed.members.size())
                    {
                        TeamMember member = new TeamMember((Player) sender);
                        return AssignToTeam(TeamRed, member);
                    }
                    else
                    {
                        String s = ChatColor.DARK_PURPLE + "Team Is Full!";
                        sender.sendMessage(s);
                        return false;
                    }
                }
                else
                {
                    String s = ChatColor.DARK_PURPLE + "Already joined a team!";
                    sender.sendMessage(s);
                    return false;
                }

        }
            else
            {
                String s = ChatColor.DARK_PURPLE + "Invalid Command!";
                sender.sendMessage(s);
                return false;
            }
    }
    private boolean ShowMemberNames(CommandSender sender, String[] args)
    {
        if (args[2].equalsIgnoreCase("Blue"))
        {
            for(Iterator<String> i = TeamBlue.GetMemberNames().iterator(); i.hasNext(); )
            {
                String membername = i.next();
                sender.sendMessage(membername);
            }
            return true;
        }
        else if (args[2].equalsIgnoreCase("Red"))
        {
            for(Iterator<String> i = TeamRed.GetMemberNames().iterator(); i.hasNext(); ) {
                String membername = i.next();
                sender.sendMessage(membername);
            }
            return true;
        }
        else
        {
            sender.sendMessage("Invalid Team!");
            return false;
        }
    }
    private boolean ShowPlayerInfo(CommandSender sender, String[] args)
    {
        Player p = (Player) sender;
        if (TeamBlue.FindPlayer(p)) {
            sender.sendMessage(p.getDisplayName() + " " + "is in team blue!");
            return true;
        } else if (TeamRed.FindPlayer(p)) {
            sender.sendMessage(p.getDisplayName() + " " + "is in team Red!");
            return true;
        } else {
            sender.sendMessage("Not in any team!");
            return false;
        }
    }


    public void onQuit(PlayerQuitEvent event)
    {
        RemoveFromTeam(event.getPlayer());
        //getServer().broadcastMessage("Bye!");
    }

    public Team GetPlayerTeam(Player p)
    {
        if(TeamRed.FindPlayer(p))
        {
            return TeamRed;
        }
        else if(TeamBlue.FindPlayer(p))
        {
            return TeamBlue;
        }
        else
            return null;
    }

    private void LeaveTeam(CommandSender sender)
    {
        RemoveFromTeam((Player) sender);
    }

    private boolean AssignToTeam(Team t, TeamMember member)
    {
        member.Equip(t.color);
        t.AddMember(member);
        ChatColor c;
        CTFManager.ReturnServer().broadcastMessage(member.GetPlayerName() + " " + "Joined Team " + t.name + "!");
        return true;
    }

    private void RemoveFromTeam(Player p)
    {
        Team t = GetPlayerTeam(p);
        if(t != null)
        {
            t.GetMember(p).Unequip();
            t.RemoveMember(t.GetMember(p));
            CTFManager.ReturnServer().broadcastMessage(p.getDisplayName() + " left Team " + t.name + "!");
        }
        else
            p.sendMessage("Not in a team!");

    }

    public static int randInt(int min, int max) {

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}


