import { Collection } from '@nozbe/watermelondb';
import { database, Post, BlogUser, PostLike } from '@/database';
import EventSourceHandler from '../EventSourceHandler';
import moment from 'moment';

class PostLikeHandler extends EventSourceHandler {

  private collection: Collection<PostLike>;

  constructor() {
    super(Post.table)
    this.collection = database.get(PostLike.table);
  }

  public async onCreated(item: Record<string, string>): Promise<void> {
    await database.write(async () => {
      const user = await database.get<BlogUser>(BlogUser.table).find(item.userId);
      const post = await database.get<Post>(Post.table).find(item.postId);
      await this.collection.create((like) => {
        like._raw.id = item.id;
        like._raw._status = 'synced';
        like.createdAt = moment(item.created_at_utc).toDate();
        like.updatedAt = moment(item.updated_at_utc).toDate();
        like.user.set(user);
        like.post.set(post);
      });
    });
  }
}

export default PostLikeHandler;