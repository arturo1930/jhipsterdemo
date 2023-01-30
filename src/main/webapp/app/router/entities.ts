import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

// prettier-ignore
const HistoriaUsuario = () => import('@/entities/historia-usuario/historia-usuario.vue');
// prettier-ignore
const HistoriaUsuarioUpdate = () => import('@/entities/historia-usuario/historia-usuario-update.vue');
// prettier-ignore
const HistoriaUsuarioDetails = () => import('@/entities/historia-usuario/historia-usuario-details.vue');
// prettier-ignore
const Tarea = () => import('@/entities/tarea/tarea.vue');
// prettier-ignore
const TareaUpdate = () => import('@/entities/tarea/tarea-update.vue');
// prettier-ignore
const TareaDetails = () => import('@/entities/tarea/tarea-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'historia-usuario',
      name: 'HistoriaUsuario',
      component: HistoriaUsuario,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'historia-usuario/new',
      name: 'HistoriaUsuarioCreate',
      component: HistoriaUsuarioUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'historia-usuario/:historiaUsuarioId/edit',
      name: 'HistoriaUsuarioEdit',
      component: HistoriaUsuarioUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'historia-usuario/:historiaUsuarioId/view',
      name: 'HistoriaUsuarioView',
      component: HistoriaUsuarioDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tarea',
      name: 'Tarea',
      component: Tarea,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tarea/new',
      name: 'TareaCreate',
      component: TareaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tarea/:tareaId/edit',
      name: 'TareaEdit',
      component: TareaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tarea/:tareaId/view',
      name: 'TareaView',
      component: TareaDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
