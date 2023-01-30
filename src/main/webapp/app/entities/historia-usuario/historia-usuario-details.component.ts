import { Component, Vue, Inject } from 'vue-property-decorator';

import { IHistoriaUsuario } from '@/shared/model/historia-usuario.model';
import HistoriaUsuarioService from './historia-usuario.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class HistoriaUsuarioDetails extends Vue {
  @Inject('historiaUsuarioService') private historiaUsuarioService: () => HistoriaUsuarioService;
  @Inject('alertService') private alertService: () => AlertService;

  public historiaUsuario: IHistoriaUsuario = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.historiaUsuarioId) {
        vm.retrieveHistoriaUsuario(to.params.historiaUsuarioId);
      }
    });
  }

  public retrieveHistoriaUsuario(historiaUsuarioId) {
    this.historiaUsuarioService()
      .find(historiaUsuarioId)
      .then(res => {
        this.historiaUsuario = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
