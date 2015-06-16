package CTF.Teams;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by Michael on 4/28/2015.
 */
public class Team {

    public List<TeamMember> members ;
    public String name;
    public Color color;

    public Team(){
        members = new ArrayList();

    }

    public Team(String n){
        members = new ArrayList();
        name = n;
        color = Color.GRAY;
    }

    public Team(String n, Color c){
        members = new ArrayList();
        name = n;
        color = c;
    }


    public List<String> GetMemberNames()
    {
        List<String> names = new ArrayList();

        for(int i = 0; i < members.size(); i++)
        {
            names.add(members.get(i).GetPlayerName());
        }
        return names;
    }


    public TeamMember GetMember(Player p)
    {
        TeamMember m = null;

        for(int i = 0; i < members.size(); i++)
        {
           if(members.get(i).GetPlayer() == p)
               m = members.get(i);
        }
        return m;
    }

    public void ResetMembers()
    {
        members.clear();
    }

    public void AddMember(TeamMember m)
    {
        members.add(m);
    }


    public boolean FindPlayer(Player p)
    {
        boolean hasPlayer = false;
        for(TeamMember m : members)
        {
            if(p == m.GetPlayer())
                hasPlayer = true;
        }
        return hasPlayer;
    }
    public void RemoveMember(TeamMember m)
    {
        members.remove(m);
    }

}
