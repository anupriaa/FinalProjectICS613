package controllers;

import models.FileEntry;
import models.FileInfo;

import play.mvc.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Searches the images related to entered keyword.
 */
public class SearchFiles extends Controller {
  /**
   * Queries the database for images related to the logged in user.
   * @return the list of images.
   */
  public static List<FileInfo> searchAllFiles(String fileType) {

    //getSynonyms(queryKeywords);

    ArrayList<Long> finalIdList = new ArrayList<Long>();
    List<FileInfo> fileIdList = new ArrayList<FileInfo>();

    String email = Secured.getUser(ctx());
    List<FileEntry> entryIdList = FileEntry.find()
        .select("entryId")
        .where()
        .eq("email", email)
        //.eq("entryType", fileType)
        .findList();
    for (FileEntry entry : entryIdList) {
      finalIdList.add(entry.getEntryId());
    }
    System.out.println("finalIdList for "+fileType+"---" + finalIdList);
    if(fileType.equalsIgnoreCase("all")){
     fileIdList = FileInfo.find()
          .select("fileId")
          .where()
          .in("fileEntryId", finalIdList)
          .findList();
    } else{
      fileIdList = FileInfo.find()
          .select("fileId")
          .where()
          .in("fileEntryId", finalIdList)
          .eq("fileType", fileType)
          .findList();
    }
    ArrayList<String> images = new ArrayList<String>();
    for (FileInfo imageInfo : fileIdList) {
      images.add(imageInfo.getFileType());
    }
    System.out.println("filetypes in search----" + images);
    return fileIdList;
  }
}
