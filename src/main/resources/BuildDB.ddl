CREATE TABLE Users (
  handle VARCHAR(32) NOT NULL,
  password VARCHAR(255) NOT NULL,
  PRIMARY KEY(handle)
)

CREATE TABLE Posts (
  id INT NOT NULL AUTO_INCREMENT,
  content VARCHAR(255) NOT NULL,
  date DATE,
  PRIMARY KEY(id)
)

CREATE TABLE UserLikes (
  userHandle VARCHAR(32) NOT NULL,
  postId INT NOT NULL,
  CONSTRAINT PK_UserLike PRIMARY KEY(userHandle,postId),
  FOREIGN KEY (userHandle) REFERENCES Users(handle),
  FOREIGN KEY (postId) REFERENCES Posts(id)
)