import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, numeric } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import TareaService from '@/entities/tarea/tarea.service';
import { ITarea } from '@/shared/model/tarea.model';

import { IHistoriaUsuario, HistoriaUsuario } from '@/shared/model/historia-usuario.model';
import HistoriaUsuarioService from './historia-usuario.service';

const validations: any = {
  historiaUsuario: {
    nombre: {
      required,
    },
    descripcion: {
      required,
    },
    criteriosAceptacion: {
      required,
      numeric,
    },
  },
};

@Component({
  validations,
})
export default class HistoriaUsuarioUpdate extends Vue {
  @Inject('historiaUsuarioService') private historiaUsuarioService: () => HistoriaUsuarioService;
  @Inject('alertService') private alertService: () => AlertService;

  public historiaUsuario: IHistoriaUsuario = new HistoriaUsuario();

  @Inject('tareaService') private tareaService: () => TareaService;

  public tareas: ITarea[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.historiaUsuarioId) {
        vm.retrieveHistoriaUsuario(to.params.historiaUsuarioId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.historiaUsuario.id) {
      this.historiaUsuarioService()
        .update(this.historiaUsuario)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('jhipsterdemoApp.historiaUsuario.updated', { param: param.id });
          return (this.$root as any).$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.historiaUsuarioService()
        .create(this.historiaUsuario)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('jhipsterdemoApp.historiaUsuario.created', { param: param.id });
          (this.$root as any).$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public retrieveHistoriaUsuario(historiaUsuarioId): void {
    this.historiaUsuarioService()
      .find(historiaUsuarioId)
      .then(res => {
        this.historiaUsuario = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.tareaService()
      .retrieve()
      .then(res => {
        this.tareas = res.data;
      });
  }
}
