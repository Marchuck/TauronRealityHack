package pl.marczak.tauronrealityhack.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class QuizResponse {
  
  @SerializedName("Id")
  private Integer id = null;
  @SerializedName("ExpirationDate")
  private String expirationDate = null;
  @SerializedName("Header")
  private String header = null;
  @SerializedName("ImageUri")
  private String imageUri = null;
  @SerializedName("Question")
  private String question = null;
  @SerializedName("Answers")
  private List<AnswerShortResponse> answers = null;
  public enum QuizOriginTypeEnum {
     Unknown,  FromGame,  FromNews,  FromChallenge,  FromChallengeRevenge, 
  };
  @SerializedName("QuizOriginType")
  private QuizOriginTypeEnum quizOriginType = null;
  @SerializedName("IsFilled")
  private Boolean isFilled = null;
  @SerializedName("ChallengeId")
  private Integer challengeId = null;

  
  /**
   * Id of news
   **/
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }

  
  /**
   * Date until quiz is valid
   **/
 public String getExpirationDate() {
    return expirationDate;
  }
  public void setExpirationDate(String expirationDate) {
    this.expirationDate = expirationDate;
  }

  
  /**
   * Quiz header
   **/

  public String getHeader() {
    return header;
  }
  public void setHeader(String header) {
    this.header = header;
  }

  
  /**
   * Quiz image uri
   **/

  public String getImageUri() {
    return imageUri;
  }
  public void setImageUri(String imageUri) {
    this.imageUri = imageUri;
  }

  
  /**
   * Quiz question
   **/

  public String getQuestion() {
    return question;
  }
  public void setQuestion(String question) {
    this.question = question;
  }

  
  /**
   **/

  public List<AnswerShortResponse> getAnswers() {
    return answers;
  }
  public void setAnswers(List<AnswerShortResponse> answers) {
    this.answers = answers;
  }

  
  /**
   * Origin type
   **/
  public QuizOriginTypeEnum getQuizOriginType() {
    return quizOriginType;
  }
  public void setQuizOriginType(QuizOriginTypeEnum quizOriginType) {
    this.quizOriginType = quizOriginType;
  }

  
  /**
   * Has Quiz already filled
   **/
  public Boolean getIsFilled() {
    return isFilled;
  }
  public void setIsFilled(Boolean isFilled) {
    this.isFilled = isFilled;
  }

  
  /**
   * Id of challenge
   **/
  public Integer getChallengeId() {
    return challengeId;
  }
  public void setChallengeId(Integer challengeId) {
    this.challengeId = challengeId;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuizResponse {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  expirationDate: ").append(expirationDate).append("\n");
    sb.append("  header: ").append(header).append("\n");
    sb.append("  imageUri: ").append(imageUri).append("\n");
    sb.append("  question: ").append(question).append("\n");
    sb.append("  answers: ").append(answers).append("\n");
    sb.append("  quizOriginType: ").append(quizOriginType).append("\n");
    sb.append("  isFilled: ").append(isFilled).append("\n");
    sb.append("  challengeId: ").append(challengeId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
