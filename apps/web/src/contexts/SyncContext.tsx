import React, { PropsWithChildren, useEffect, useMemo } from 'react';
import { fetchEventSource as eventsource } from '@microsoft/fetch-event-source';
import { authenticatedFetch } from '@/libs/fetch';
import { v4 } from 'uuid';
import EventSourceConnection from '@/database/eventsource/EventSourceConnection';
import { useOrganization } from './OrganizationContext';

const SyncProvider: React.FC<PropsWithChildren> = ({ children }) => {
  const eventId = useMemo(() => v4(), []);
  const { organization } = useOrganization();

  useEffect(() => {
    if (!organization) return;
  }, [organization]);

  useEffect(() => {
    if (!organization?.id) return;
    const controller = new AbortController();

    eventsource(`organizations/${organization.id}/changes`, {
      ...new EventSourceConnection(eventId),
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