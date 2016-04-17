import play.GlobalSettings;
import play.api.mvc.Results.Status;
import play.libs.F.Promise;
import play.libs.Scala;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import scala.Tuple2;
import scala.collection.Seq;

import java.util.ArrayList;
import java.util.List;

import static play.core.j.JavaResults.BadRequest;
import static play.core.j.JavaResults.InternalServerError;
import static play.core.j.JavaResults.NotFound;
/**
 * Stores the Global settings of the project.
 */
public class Global extends GlobalSettings {
  /*public <T extends EssentialFilter> Class<T>[] filters() {
    return new Class[]{SecurityHeadersFilter.class};
  }*/

  /**
   * Action wrapper class.
   */
  private class ActionWrapper extends Action.Simple {
    /**
     * Action wrapper for CORS functionality.
     * @param action the action taken.
     */
    public ActionWrapper(Action<?> action) {

      this.delegate = action;
    }

    @Override
    public Promise<Result> call(Http.Context ctx) throws java.lang.Throwable {
      Promise<Result> result = this.delegate.call(ctx);
      Http.Response response = ctx.response();
      response.setHeader("Access-Control-Allow-Origin", "*");
      return result;
    }
  }

  /*
  * Adds the required CORS header "Access-Control-Allow-Origin" to successfull requests
  */
  @Override
  public Action<?> onRequest(Http.Request request, java.lang.reflect.Method actionMethod) {
    return new ActionWrapper(super.onRequest(request, actionMethod));
  }

  /**
   * class to implement CORS.
   */
  private static class CORSResult implements Result {
    /**
     * Play result.
     */
    final private play.api.mvc.Result wrappedResult;

    /**
     * CORS result.
     * @param status the status of the action.
     */
    public CORSResult(Status status) {
      List<Tuple2<String, String>> list = new ArrayList<Tuple2<String, String>>();
      Tuple2<String, String> t = new Tuple2<String, String>("Access-Control-Allow-Origin", "*");
      list.add(t);
      Seq<Tuple2<String, String>> seq = Scala.toSeq(list);
      wrappedResult = status.withHeaders(seq);
    }

    /**
     * Converts result to scala.
     * @return the wrapped result.
     */
    public play.api.mvc.Result toScala() {
      return this.wrappedResult;
    }
  }

  /*
  * Adds the required CORS header "Access-Control-Allow-Origin" to bad requests
  */
  @Override
  public Promise<Result> onBadRequest(Http.RequestHeader request, String error) {
    return Promise.<Result>pure(new CORSResult(BadRequest()));
  }

  /*
  * Adds the required CORS header "Access-Control-Allow-Origin" to requests that causes an exception
  */
  @Override
  public Promise<Result> onError(Http.RequestHeader request, Throwable t) {
    return Promise.<Result>pure(new CORSResult(InternalServerError()));
  }

  /*
  * Adds the required CORS header "Access-Control-Allow-Origin" when a route was not found
  */
  @Override
  public Promise<Result> onHandlerNotFound(Http.RequestHeader request) {
    return Promise.<Result>pure(new CORSResult(NotFound()));
  }


}
