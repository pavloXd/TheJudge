package com.example.aplicacion.services;


import com.example.aplicacion.Entities.Team;
import com.example.aplicacion.Entities.User;
import com.example.aplicacion.Pojos.TeamString;
import com.example.aplicacion.Repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserService userService;

    public TeamString crearTeam(String nickTeam, boolean isUser){
        TeamString salida = new TeamString();

        Team team = new Team(nickTeam);
        team.setEsUser(isUser);
       if(teamRepository.existsByNombreEquipo(nickTeam)){
           salida.setSalida("TEAM NAME DUPLICATED");
            return salida;
        }
        else {
            teamRepository.save(team);
            salida.setSalida("OK");
            salida.setTeam(team);
            return salida;
        }
    }

    public String addUserToTeam(Team team, User user){

        if(teamRepository.existsTeamByParticipantesContains(user)){
            return "USUARIO YA APUNTADO";
        }
        else {
            team.addUserToTeam(user);
            teamRepository.save(team);
            return "OK";
        }

    }

    public String deleteTeamByName(String name){
        Team team = teamRepository.findByNombreEquipo(name);
        if(team==null){
            return "TEAM NOT FOUND";
        }else {
            //Si es un team user borramos el user
            if(team.isEsUser()){
                userService.deleteUserByNickname(team.getNombreEquipo());
            }
            teamRepository.delete(team);
            return "OK";
        }
    }

    public String deleteTeamByTeamId(String teamId){
        Team team = teamRepository.findTeamById(Long.valueOf(teamId));
        if(team==null){
            return "TEAM NOT FOUND";
        }else {
            //Si es un team user borramos el user
            if(team.isEsUser()){
                userService.deleteUserByNickname(team.getNombreEquipo());
            }
            teamRepository.delete(team);
            return "OK";
        }
    }
    public Team getTeamByNick(String nick){
        return teamRepository.findByNombreEquipo(nick);
    }
    public List<Team> getAllTeams(){
        return teamRepository.findAll();
    }

    public Team getTeamFromId(String teamId){
        return teamRepository.findTeamById(Long.valueOf(teamId));
    }

    public TeamString addUserToTeamUssingIds(String teamId, String userId){
        TeamString salida = new TeamString();
        Team team = getTeamFromId(teamId);
        if(team == null){
            salida.setSalida("TEAM NOT FOUND");
            return salida;
        }

        User user = userService.getUserById(Long.valueOf(userId));
        if (user == null){
            salida.setSalida("USER NOT FOUND");
            return salida;
        }

        if(team.getParticipantes().contains(user)){
            salida.setSalida("USER ALREADY IN TEAM");
            return salida;
        }

        team.getParticipantes().add(user);

        teamRepository.save(team);

        salida.setSalida("OK");
        salida.setTeam(team);
        return salida;
    }

    public String deleteUserFromTeam(String teamId, String userId){
        TeamString salida = new TeamString();
        Team team = getTeamFromId(teamId);
        if(team == null){
            salida.setSalida("TEAM NOT FOUND");
            return "TEAM NOT FOUND";
        }

        User user = userService.getUserById(Long.valueOf(userId));
        if (user == null){
            salida.setSalida("USER NOT FOUND");
            return "USER NOT FOUND";
        }

        if(!team.getParticipantes().contains(user)){
            salida.setSalida("USER ALREADY IN TEAM");
            return "USER IS NOT IN TEAM";
        }

        team.getParticipantes().remove(user);
        teamRepository.save(team);

        return "OK";
    }

    public TeamString updateTeam(String teamId, Optional<String> teamName){
        TeamString salida = new TeamString();
        Team team = getTeamFromId(teamId);
        if(team == null){
            salida.setSalida("TEAM NOT FOUND");
            return salida;
        }

        if (teamName.isPresent()){
            if(teamRepository.existsByNombreEquipo(teamName.get())){
                salida.setSalida("TEAM NAME DUPLICATED");
                return salida;
            }
            else {
                team.setNombreEquipo(teamName.get());
                teamRepository.save(team);
            }
        }


        teamRepository.save(team);
        salida.setSalida("OK");
        salida.setTeam(team);
        return salida;

    }
}
