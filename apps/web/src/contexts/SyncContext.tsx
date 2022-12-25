import React, { PropsWithChildren, useEffect, useMemo } from 'react';
import { fetchEventSource as eventsource } from '@microsoft/fetch-event-source';
import { synchronize } from '@nozbe/watermelondb/sync'
import { authenticatedFetch, fetcher, poster } from '@/libs/fetch';
import { v4 } from 'uuid';
import EventSourceConnection from '@/database/eventsource/EventSourceConnection';
import { database, SyncChanges } from '@/database';
import { useOrganization } from './OrganizationContext';

type SyncObjects= {
  changes: SyncChanges;
  timestamp: number;
}

const SyncProvider: React.FC<PropsWithChildren> = ({ children }) => {
  const eventId = useMemo(() => v4(), []);
  const { organization } = useOrganization();

  useEffect(() => {
    if (!organization) return;

    synchronize({
      database: database,
      pullChanges: async ({ lastPulledAt }) => {
        return fetcher<SyncObjects>(`/organizations/${organization.id}/sync?last_pulled_at=${lastPulledAt || 0}`)
      },
      pushChanges: async ({ changes, lastPulledAt}) => {
        /*poster(`/organizations/${organization.id}/sync?last_pulled_at=${lastPulledAt || 0}`, {
          changes
        })*/
      },
    })
  }, [organization]);

  useEffect(() => {
    if (!organization?.id) return;
    const controller = new AbortController();

    eventsource(`organizations/${organization.id}/changes`, {
      ...new EventSourceConnection(eventId, controller),
      signal: controller.signal,
      fetch: authenticatedFetch
    });

    return () => {
      controller.abort();
    };
  }, [organization]);

  return <>{children}</>;
};

export default SyncProvider;