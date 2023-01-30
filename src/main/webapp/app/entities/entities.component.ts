import { Component, Provide, Vue } from 'vue-property-decorator';

import UserService from '@/entities/user/user.service';
import HistoriaUsuarioService from './historia-usuario/historia-usuario.service';
import TareaService from './tarea/tarea.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

@Component
export default class Entities extends Vue {
  @Provide('userService') private userService = () => new UserService();
  @Provide('historiaUsuarioService') private historiaUsuarioService = () => new HistoriaUsuarioService();
  @Provide('tareaService') private tareaService = () => new TareaService();
  // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
}
