import { Collection } from '@nozbe/watermelondb';
import { database, Post, BlogUser } from '@/database';
import EventSourceHandler from '../EventSourceHandler';
import moment from 'moment';

class PostHandler extends EventSourceHandler {

  private collection: Collection<Post>;

  constructor() {
    super(Post.table)
    this.collection = database.get(Post.table);
  }

  public async onCreated(item: Record<string, string>): Promise<void> {
    await database.write(async () => {
      const user = await database.get<BlogUser>(BlogUser.table).find(item.user_id);
      await this.collection.create((post) => {
        post._raw.id = item.id;
        post._raw._status = 'synced';
        
        post.title = item.title;
        post.subtitle = item.subtitle;
        post.content = item.content;
        post.createdAt = moment(item.created_at_utc).toDate();
        post.updatedAt = moment(item.updated_at_utc).toDate();
        post.user.set(user);
      });
    });
  }
}

export default PostHandler;