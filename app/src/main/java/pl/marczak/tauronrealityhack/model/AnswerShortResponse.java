package pl.marczak.tauronrealityhack.model;


import com.google.gson.annotations.SerializedName;


public class AnswerShortResponse {
  
  @SerializedName("Id")
  private Integer id = null;
  @SerializedName("Answer")
  private String answer = null;

  
  /**
   **/
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }

  
  /**
   **/
  public String getAnswer() {
    return answer;
  }
  public void setAnswer(String answer) {
    this.answer = answer;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnswerShortResponse {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  answer: ").append(answer).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
