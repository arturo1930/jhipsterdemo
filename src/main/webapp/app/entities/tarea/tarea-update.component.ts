import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';
import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';

import HistoriaUsuarioService from '@/entities/historia-usuario/historia-usuario.service';
import { IHistoriaUsuario } from '@/shared/model/historia-usuario.model';

import { ITarea, Tarea } from '@/shared/model/tarea.model';
import TareaService from './tarea.service';

const validations: any = {
  tarea: {
    nombre: {
      required,
    },
    actividades: {},
    fechaCreacion: {},
  },
};

@Component({
  validations,
})
export default class TareaUpdate extends Vue {
  @Inject('tareaService') private tareaService: () => TareaService;
  @Inject('alertService') private alertService: () => AlertService;

  public tarea: ITarea = new Tarea();

  @Inject('historiaUsuarioService') private historiaUsuarioService: () => HistoriaUsuarioService;

  public historiaUsuarios: IHistoriaUsuario[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.tareaId) {
        vm.retrieveTarea(to.params.tareaId);
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
    if (this.tarea.id) {
      this.tareaService()
        .update(this.tarea)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('jhipsterdemoApp.tarea.updated', { param: param.id });
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
      this.tareaService()
        .create(this.tarea)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('jhipsterdemoApp.tarea.created', { param: param.id });
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

  public convertDateTimeFromServer(date: Date): string {
    if (date && dayjs(date).isValid()) {
      return dayjs(date).format(DATE_TIME_LONG_FORMAT);
    }
    return null;
  }

  public updateInstantField(field, event) {
    if (event.target.value) {
      this.tarea[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.tarea[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.tarea[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.tarea[field] = null;
    }
  }

  public retrieveTarea(tareaId): void {
    this.tareaService()
      .find(tareaId)
      .then(res => {
        res.fechaCreacion = new Date(res.fechaCreacion);
        this.tarea = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.historiaUsuarioService()
      .retrieve()
      .then(res => {
        this.historiaUsuarios = res.data;
      });
  }
}
