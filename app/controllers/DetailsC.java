package controllers;

import play.*;
import play.mvc.*;
import play.data.*;

// added
import play.i18n.Messages;
import views.html.detailsIH;
import models.User;
import models.Ban;
import java.text.SimpleDateFormat;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.File;

public class DetailsC extends Controller {

  @Security.Authenticated(SecuredUser.class)
  public static Result details(Long id) {
    // fixme: not found user

    User banner = User.find.where().eq("email",session("email")).findUnique();
    User user=User.find.where().eq("id",id).findUnique();
    Ban b=Ban.find.where().eq("banner_id",banner.id).eq("banned_id",user.id).findUnique();
    if(b==null){
      b=Ban.find.where().eq("banner_id",user.id).eq("banned_id",banner.id).findUnique(); 
    }
    if(b!=null){
      flash("error",new Messages().get("ban.exists"));
      return redirect(routes.HomeC.home());
    }

    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    String date = DATE_FORMAT.format(user.birth);
    String ip="";
    try {
      ip = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      System.out.println("Some problem getting ip");
      e.printStackTrace();
    }    
    return ok(detailsIH.render(user,date,ip));
  }

  @Security.Authenticated(SecuredUser.class)
  public static Result myDetails() {
    User user = User.find.where().eq("email",session("email")).findUnique();
    return redirect(routes.DetailsC.details(user.id)); 
  }

}
